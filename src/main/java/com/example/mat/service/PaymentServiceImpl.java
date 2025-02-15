package com.example.mat.service;

import com.example.mat.config.ImportConfig;
import com.example.mat.config.IamportConfig;
import com.example.mat.entity.market.Order;
import com.example.mat.entity.market.Payment;
import com.example.mat.entity.constant.PaymentStatus;
import com.example.mat.repository.OrderRepository;
import com.example.mat.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ImportConfig importConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // ✅ Jackson ObjectMapper 사용

    // ✅ 아임포트 액세스 토큰 가져오기
    private String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken";

        Map<String, String> requestData = Map.of(
                "imp_key", importConfig.getApiKey(),
                "imp_secret", importConfig.getSecretKey());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestData, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        try {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return jsonResponse.path("response").path("access_token").asText();
        } catch (Exception e) {
            log.error("Failed to parse access token response", e);
            throw new RuntimeException("토큰 조회 실패");
        }
    }

    // ✅ 아임포트 결제 검증
    public boolean validatePayment(String impUid, int paidAmount) {
        String accessToken = getAccessToken();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        try {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            int amountFromApi = jsonResponse.path("response").path("amount").asInt();

            return amountFromApi == paidAmount;
        } catch (Exception e) {
            log.error("결제 검증 중 오류 발생", e);
            return false;
        }
    }

    // ✅ 결제 정보 저장 및 주문 상태 변경
    public void savePayment(String impUid, Long orderId, int paidAmount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        boolean isValid = validatePayment(impUid, paidAmount);
        if (!isValid) {
            throw new IllegalStateException("결제 검증 실패");
        }

        Payment payment = Payment.builder()
                .member(order.getMember())
                .order(order)
                .paymentUid(impUid)
                .totalPrice(paidAmount)
                .paymentStatus(PaymentStatus.OK)
                .build();

        paymentRepository.save(payment);
        order.setPayment(payment);
        order.setOrderStatus(com.example.mat.entity.constant.OrderStatus.ORDER);
        orderRepository.save(order);

        log.info("결제 성공! 결제 ID: {}, 주문 ID: {}", payment.getId(), order.getOid());
    }
}
