package com.example.mat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mat.repository.CartItemRepository;
import com.example.mat.repository.CartRepository;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    PaymentService paymentService;

}
