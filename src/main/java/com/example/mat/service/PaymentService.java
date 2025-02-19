package com.example.mat.service;

import com.example.mat.entity.market.Cart;

public interface PaymentService {

    // 결제 검증 메서드
    boolean validatePayment(String impUid, int paidAmount);

    void savePayment(String impUid, Long orderId, int paidAmount);

}
