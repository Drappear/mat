package com.example.mat.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mat.entity.constant.OrderStatus;
import com.example.mat.entity.market.Order;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    public void NewOrder(){
        Order order = Order.builder()
        .name("홍길동")
        .price(20000)
        .quantity(2)
        .orderStatus(OrderStatus.ORDER)
        .build();

        orderRepository.save(order);

    }
    
}
