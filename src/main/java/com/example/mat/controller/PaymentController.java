package com.example.mat.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mat.entity.constant.OrderStatus;
import com.example.mat.entity.constant.PaymentStatus;
import com.example.mat.entity.market.Order;
import com.example.mat.entity.market.Payment;
import com.example.mat.repository.OrderRepository;
import com.example.mat.repository.PaymentRepository;
import com.example.mat.service.PaymentService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/market/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> paymentData) {
        try {
            String impUid = (String) paymentData.get("imp_uid");
            String merchantUid = (String) paymentData.get("merchant_uid");
            int paidAmount = (int) paymentData.get("amount");

            // 1. 주문 정보 가져오기
            Order order = orderRepository.findOrderAndPayment(merchantUid)
                    .orElseThrow(() -> new EntityNotFoundException("해당 주문을 찾을 수 없습니다."));

            // 2. 결제 금액 검증 (아임포트 API 요청)
            boolean isValid = paymentService.validatePayment(impUid, paidAmount);

            if (!isValid) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "결제 금액 불일치"));
            }

            // 3. 결제 정보 저장 및 주문 상태 변경
            Payment payment = Payment.builder()
                    .member(order.getMember())
                    .order(order)
                    .paymentUid(impUid)
                    .totalPrice(paidAmount)
                    .paymentStatus(PaymentStatus.OK)
                    .build();
            paymentRepository.save(payment);

            order.setPayment(payment);
            order.setOrderStatus(OrderStatus.ORDER); // 결제 완료 상태 변경
            orderRepository.save(order);

            return ResponseEntity.ok(Map.of("success", true, "orderId", order.getOid()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
