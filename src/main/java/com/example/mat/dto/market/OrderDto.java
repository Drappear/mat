package com.example.mat.dto.market;

import java.util.List;

import com.example.mat.entity.constant.OrderStatus;
import com.example.mat.entity.market.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDto {

    private Long oid;

    private int price;

    private int quantity;

    private String name;

    private String phoneNumber;

    private String zipcode;

    private String addr;

    private String detailAddr;

    private String email;

    private OrderStatus orderStatus;

    private Product product;
    private List<OrderItemDto> orderItems; // ✅ DTO 리스트로 변경

}
