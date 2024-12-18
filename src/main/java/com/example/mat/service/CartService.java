package com.example.mat.service;

import java.util.ArrayList;
import java.util.List;

import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.dto.market.CartItemDto;

public interface CartService {

    // 장바구니 상품 추가
    public Long addCart(CartItemDto cartItemDto, String email);

    // mid 이용하여 카트 리스트 조회
    //public List<CartDetailDto> getCartList = new ArrayList<>();
    public List<CartDetailDto> getCartList(String email);
  

    // 장바구니 수량 업데이트
    public void updateCartItemQuantity(Long cartitemid, int quantity);

    // 장바구니 아이템 제거
    public void deleteCartItem(Long cartitemid);



    
}
