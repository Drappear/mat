package com.example.mat.entity.market;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.constant.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class Order extends BaseEntity {

    private Long oid;

    private int price;

    private int quantity;

    // 새로운 배송지 입력한 경우
    private String name;
    private String phoneNumber;
    private String zipcode;
    private String addr;
    private String detailAddr;
    private String email;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}
