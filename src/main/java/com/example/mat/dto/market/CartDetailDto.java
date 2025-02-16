package com.example.mat.dto.market;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDetailDto {

    // 장바구니 조회 페이지로 전달
    // 장바구니에 담겨있는 상품 조회

    private Long cartItemId; 

    // 상품명
    private String itemName;

    // 가격
    private int price;

    // 수량
    private int quantity;

    private Long cartId; 

    private int productQuantity;

    // 합산가격
    public Integer getTotalPrice() {
        return (price != 0 && quantity != 0) ? price * quantity : 0;
    }

    // 카트 페이지 합산가격
    public void setTotalPrice(int totalPrice) {
    }

    
}
