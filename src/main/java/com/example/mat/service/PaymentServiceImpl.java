package com.example.mat.service;

import com.example.mat.config.ImportConfig;
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

        // API Key 및 Secret Key 확인
        String apiKey = importConfig.getApiKey();
        String secretKey = importConfig.getSecretKey();

        log.info("아임포트 API Key: {}", apiKey);
        log.info("아임포트 Secret Key: {}", secretKey);

        if (apiKey == null || secretKey == null || apiKey.isBlank() || secretKey.isBlank()) {
            throw new IllegalStateException("아임포트 API 키 또는 시크릿 키가 설정되지 않았습니다.");
        }

        Map<String, String> requestData = new HashMap<>();
        requestData.put("imp_key", apiKey);
        requestData.put("imp_secret", secretKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestData, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("아임포트 API 응답 오류: {}", response.getStatusCode());
                throw new RuntimeException("아임포트 토큰 요청 실패: " + response.getStatusCode());
            }

            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            String accessToken = jsonResponse.path("response").path("access_token").asText();

            if (accessToken == null || accessToken.isEmpty()) {
                throw new RuntimeException("아임포트 액세스 토큰이 응답에 없음!");
            }

            log.info("아임포트 액세스 토큰 발급 성공: {}", accessToken);
            return accessToken;

        } catch (Exception e) {
            log.error("🚨 아임포트 API 호출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("아임포트 토큰 조회 실패");
        }
    }

    // ✅ 아임포트 결제 검증
    @Override
    public boolean validatePayment(String impUid, int paidAmount) {
        String accessToken = getAccessToken();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        try {
            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("🚨 아임포트 API 응답 오류: {}", response.getStatusCode());
                return false;
            }

            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            log.info("🔍 아임포트 응답: {}", response.getBody());

            JsonNode responseNode = jsonResponse.path("response");
            if (responseNode.isMissingNode() || responseNode.isNull()) {
                log.error("🚨 아임포트 API 응답 데이터가 없음!");
                return false;
            }

            int amountFromApi = responseNode.path("amount").asInt(0);
            log.info("✅ API에서 조회한 결제 금액: {}", amountFromApi);
            log.info("✅ 클라이언트에서 받은 결제 금액: {}", paidAmount);

            return amountFromApi == paidAmount;

        } catch (Exception e) {
            log.error("🚨 결제 검증 중 오류 발생", e);
            return false;
        }
    }

    // ✅ 결제 정보 저장 및 주문 상태 변경
    @Override
    public void savePayment(String impUid, Long orderId, int paidAmount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("🚨 주문을 찾을 수 없습니다. orderId=" + orderId));

        boolean isValid = validatePayment(impUid, paidAmount);
        if (!isValid) {
            throw new IllegalStateException("❌ 결제 검증 실패: 결제 금액 불일치");
        }

        // ✅ member_mid 및 order_oid 값 검증
        if (order.getMember() == null || order.getMember().getMid() == null) {
            throw new RuntimeException("❌ member_mid 값이 NULL입니다!");
        }
        if (order.getOid() == null) {
            throw new RuntimeException("❌ order_oid 값이 NULL입니다!");
        }

        log.info("✅ 결제 저장 시작 - 주문 ID: {}, 결제 UID: {}, 결제 금액: {}", orderId, impUid, paidAmount);

        try {
            Payment payment = Payment.builder()
                    .member(order.getMember())
                    .order(order)
                    .paymentUid(impUid)
                    .totalPrice(paidAmount)
                    .product(order.getProduct())
                    .paymentStatus(PaymentStatus.OK)
                    .build();

            log.info("✅ 결제 객체 생성 완료: {}", payment);
            paymentRepository.save(payment);

            // ✅ 주문 상태 업데이트
            order.setPayment(payment);
            order.setOrderStatus(com.example.mat.entity.constant.OrderStatus.ORDER);
            orderRepository.save(order);

            log.info("✅ 결제 저장 완료! 결제 ID: {}, 주문 ID: {}", payment.getId(), order.getOid());

        } catch (Exception e) {
            log.error("❌ 결제 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("결제 저장 실패");
        }
    }

}
