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

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
    private Long pid;

    @Min(value = 1, message = "최소 1개 이상 담아주세요.")
    private int quantity;
    
}
