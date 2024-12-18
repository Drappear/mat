package com.example.mat.dto.market;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemDto {
    // 장바구니에 담을 상품의 id와 수량 전달 받음

    
    private Long pid;
    
    private int quantity;
    
}
