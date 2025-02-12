package com.example.mat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.dto.market.CartItemDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.entity.market.CartItem;

import groovyjarjarantlr4.v4.parse.ANTLRParser.delegateGrammar_return;

public interface CartService {

    // 카트 상품 추가
    public Long addCart(CartItemDto cartItemDto, MemberDto memberDto);

    // mid 이용하여 카트 리스트 조회
    public List<CartDetailDto> getCartList(Long mid);
    
    // 카트 아이템 제거
    public void deleteCartItem(Long cartitemid);

    // 카트 수량 업데이트
    public Map<String, Integer>  updateCartItemQuantity(Long cartitemid, int quantity);

    public int getTotalPrice(Long cartId);


    // dto=> entity
    public default CartItem dtoToEntity(CartDetailDto dto){
        return CartItem.builder()        
        .cartItemId(dto.getCartItemId())
        .quantity(dto.getQuantity())
        .build();
    }

    // entity => dto
    public default List<CartDetailDto> entityToDto(List<CartItem> cartItems){
        return cartItems.stream().map(cartItem -> CartDetailDto.builder()
        .cartItemId(cartItem.getCartItemId())
        .itemName(cartItem.getProduct().getName())
        .price(cartItem.getProduct().getPrice())
        .quantity(cartItem.getQuantity())
        .productQuantity(cartItem.getProduct().getQuantity())
        .build()).collect(Collectors.toList());
    }


    
}
