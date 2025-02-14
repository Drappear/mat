package com.example.mat.dto.market;

import com.example.mat.entity.market.Order;
import com.example.mat.entity.market.Product;
import com.example.mat.entity.shin.Member;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentDto {

    private Long id; // PK

    private Member member; // 사용자
    
    private Order order;
    
    private Product product;

    private int price; // 상품 가격
    
    private Long totalPrice; // 결제한 총 가격

    private Boolean status = true; // 상태
    
}
