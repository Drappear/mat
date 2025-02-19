package com.example.mat.controller;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mat.dto.shin.AuthMemberDto;
import com.example.mat.entity.constant.OrderStatus;
import com.example.mat.entity.constant.PaymentStatus;
import com.example.mat.entity.market.Order;
import com.example.mat.entity.market.Payment;
import com.example.mat.repository.OrderRepository;
import com.example.mat.repository.PaymentRepository;
import com.example.mat.service.PaymentService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/market/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> paymentData) {

        try {
            String impUid = (String) paymentData.get("imp_uid");
            String merchantUid = (String) paymentData.get("merchant_uid");
            int paidAmount;
            log.info("🔍 [PaymentController] impUid={}, merchantUid={}", impUid, merchantUid);
            try {
                paidAmount = Integer.parseInt(paymentData.get("amount").toString());
                log.info("✅ [PaymentController] /verify 요청 도착! 요청 데이터: {}", paymentData);
            } catch (NumberFormatException e) {
                System.err.println(" total_price 변환 오류: " + paymentData.get("amount"));
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "결제 금액이 올바르지 않습니다."));
            }

            System.out.println(
                    " 결제 검증 요청: imp_uid=" + impUid + ", merchant_uid=" + merchantUid + ", amount=" + paidAmount);

            // 1. `findByOrderUid` 메서드로 주문 조회
            Order order = orderRepository.findByOrderUid(merchantUid)
                    .orElseThrow(() -> {
                        System.err.println(" 주문을 찾을 수 없습니다! merchantUid=" + merchantUid);
                        return new EntityNotFoundException("해당 주문을 찾을 수 없습니다.");
                    });
            System.out.println("✅ 주문 찾음: " + order.getOid());

            // 2. `savePayment()` 호출하여 결제 저장 + 장바구니 삭제 처리
            paymentService.savePayment(impUid, order.getOid(), paidAmount);

            return ResponseEntity
                    .ok(Map.of("success", true, "orderId", order.getOid(), "orderUid", order.getOrderUid()));

        } catch (Exception e) {
            System.err.println("🚨 결제 검증 중 오류 발생: " + e.getMessage());
            e.printStackTrace(); // 🔥 오류 추적 로그 추가
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "서버 내부 오류 발생"));
        }
    }

}
