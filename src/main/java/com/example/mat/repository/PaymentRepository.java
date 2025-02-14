package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.market.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
}
