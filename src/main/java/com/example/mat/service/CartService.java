package com.example.mat.service;

import java.util.ArrayList;
import java.util.List;

import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.dto.market.CartItemDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.entity.market.CartItem;

import groovyjarjarantlr4.v4.parse.ANTLRParser.delegateGrammar_return;

public interface CartService {

    // 장바구니 상품 추가
    public Long addCart(CartItemDto cartItemDto, MemberDto memberDto);

    // 이메일을 이용하여 카트 리스트 조회
    //public List<CartDetailDto> getCartList = new ArrayList<>();
    public List<CartDetailDto> getCartList(Long mid);
  

    // 장바구니 수량 업데이트
    public void updateCartItemQuantity(Long cartitemid, int quantity);

    // 장바구니 아이템 제거
    public void deleteCartItem(Long cartitemid);


    // dto=> entity
    public default CartItem dtoToEntity(CartItemDto dto){
        return CartItem.builder()        
        .cartItemId(dto.getPid())
        .quantity(dto.getQuantity())
        .build();
    }

    // entity => dto
    public default CartItemDto entityToDto(CartItem cartItem){
        return CartItemDto.builder()
        .pid(cartItem.getCartItemId())
        .quantity(cartItem.getQuantity())
        .build();
    }
    
}
