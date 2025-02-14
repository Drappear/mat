package com.example.mat.service;

public interface PaymentService {

    // 결제 검증 메서드
    boolean validatePayment(String impUid, int paidAmount);

}
