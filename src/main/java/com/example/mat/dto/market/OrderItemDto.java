package com.example.mat.dto.market;

import com.example.mat.entity.market.OrderItem;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Long orderItemId; // 주문 상품 ID
    private Long orderId; // 주문 ID
    private Long productId; // 상품 ID
    private String productName; // 상품 이름
    private int quantity; // 주문 수량
    private int price; // 상품 가격

    public static OrderItemDto fromEntity(OrderItem orderItem) {
        return OrderItemDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .orderId(orderItem.getOrder().getOid())
                .productId(orderItem.getProduct().getPid())
                .productName(orderItem.getProduct().getName()) // ✅ 상품 엔티티에서 이름 가져오기
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
