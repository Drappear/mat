package com.example.mat.service;

import java.util.List;

import com.example.mat.dto.market.OrderDto;
import com.example.mat.entity.market.Order;

public interface OrderService {

    // 주문 생성
    public Long createOrder(Long mid, List<Long> selectedCartItemIds, List<Integer> selectedQuantities,
            String recipientName, String phoneNumber, String email,
            String zipcode, String addr, String detailAddr);

    // 주문 정보 단순 조회
    public OrderDto getOrder(Long orderId);

    // 주문 정보 조회 (결제 및 상태)
    public Order getOrderEntity(Long orderId);

    // dto > entity
    public default Order dtoToEntity(OrderDto dto) {
        return Order.builder()
                .oid(dto.getOid())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .recipientName(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .zipcode(dto.getZipcode())
                .addr(dto.getAddr())
                .detailAddr(dto.getDetailAddr())
                .email(dto.getEmail())
                .orderStatus(dto.getOrderStatus())
                .product(dto.getProduct())
                .build();
    }

    // entity > dto
    public default OrderDto entityToDto(Order order) {
        return OrderDto.builder()
                .oid(order.getOid())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .name(order.getRecipientName())
                .phoneNumber(order.getPhoneNumber())
                .zipcode(order.getZipcode())
                .addr(order.getAddr())
                .detailAddr(order.getDetailAddr())
                .email(order.getEmail())
                .orderStatus(order.getOrderStatus())
                .product(order.getProduct())
                .build();
    }

}
