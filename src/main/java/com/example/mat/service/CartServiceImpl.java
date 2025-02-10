package com.example.mat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.dto.market.CartItemDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Product;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.CartItemRepository;

import com.example.mat.repository.CartRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.ProductRepository;

import groovyjarjarantlr4.v4.parse.ANTLRParser.prequelConstruct_return;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    
    @Override
    public Long addCart(CartItemDto cartItemDto, MemberDto memberDto) {

        // 카트 생성   
        Member member = memberRepository.findById(memberDto.getMid()).get();   
        Cart cart = cartRepository.findByMember(member);

        if (cart == null) {
            // 멤버에게 카트 없으면, 카트 생성
           cart = Cart.builder().member(Member.builder().mid(memberDto.getMid()).build()).build();
           cartRepository.save(cart);
        }

        Product product = productRepository.findById(cartItemDto.getPid())
                            .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다.")); 
                            
        // 상품의 수량 확인
        int productQuantity = product.getQuantity();
        System.out.println(product.getPid());
        System.out.println(product.getQuantity());

        CartItem savedCartItem = cartItemRepository.findByProductCart(cart.getCartid(), product.getPid());

        int requestedQuantity = cartItemDto.getQuantity();

        if(savedCartItem != null){
            // 카트에 이미 상품이 있다면 기존 수량과 request 수량을 +
            int totalQuantity  = savedCartItem.getQuantity() + requestedQuantity;
            if(totalQuantity > productQuantity) {
                throw new IllegalStateException("구매 가능한 수량을 초과하였습니다");
            }

            // 수량 업데이트
            savedCartItem.addQuantity(cartItemDto.getQuantity());
            cartItemRepository.save(savedCartItem);
            return savedCartItem.getCartItemId();

        } else {
            
            if(requestedQuantity > productQuantity){
                throw new IllegalStateException("구매 가능한 수량을 초과하였습니다");
            }

            // 새로운 CartItem 생성
            CartItem cartItem =  CartItem.builder()
            .quantity(cartItemDto.getQuantity())
            .product(product)
            .cart(cart)            
            .build();
            cartItemRepository.save(cartItem);
            return cartItem.getCartItemId();
        }

    }


    public List<CartDetailDto> getCartList(Long mid){

        List<CartDetailDto> cartDetailDtoList;

        //로그인 정보로 member 찾기기    
        Member member = memberRepository.findById(mid).get();       
        Cart cart = cartRepository.findByMember(member);

        if(cart == null){ // 위에서 유저 카트 조회해서, 없으면은 그냥 반환
            return new ArrayList<>();
        }

        List<CartItem> cartItems = cartItemRepository.findByCartItem(cart);

        // entity => dto
        // cartDetailDtoList = entityToDto()
        cartDetailDtoList = entityToDto(cartItems);

        System.out.println("service");
        // System.out.println(cartDetailDtoList);
        for (CartDetailDto dto : cartDetailDtoList) {
            System.out.println(dto.getItemName());
            System.out.println(dto.getTotalPrice());
        }

        //return cartDetailDtoList; 
        return cartDetailDtoList;
    }


    @Override
    public void deleteCartItem(Long cartitemid) {

    cartItemRepository.deleteById(cartitemid);
        
    }

    @Override
    public Map<String, Integer> updateCartItemQuantity(Long cartItemId, int quantity) {
    // 카트 아이템 조회
    // CartItem cartItem = cartItemRepository.findById(cartItemId)
    //     .orElseThrow(() -> new EntityNotFoundException("카트 아이템을 찾을 수 없습니다"));

    // // 상품 최대 수량 확인
    // Product product = cartItem.getProduct();
    // int maxQuantity = product.getQuantity();
    // if (quantity > maxQuantity) {
    //     throw new IllegalStateException("구매 가능한 수량을 초과하였습니다");
    // }

    // // 수량 업데이트
    // cartItem.setQuantity(quantity);
    // cartItemRepository.save(cartItem);

    // // 개별 상품 총합 계산
    // int updatedItemTotal = cartItem.getQuantity() * product.getPrice();

    // // 카트 총합 계산
    // Cart cart = cartItem.getCart();
    // int updatedCartTotal = cartItemRepository.findByCartItem(cart).stream()
    //     .mapToInt(item -> item.getQuantity() * item.getProduct().getPrice())
    //     .sum();

    // // 결과 반환
    // Map<String, Integer> response = new HashMap<>();
    // response.put("updatedItemTotal", updatedItemTotal);
    // response.put("updatedCartTotal", updatedCartTotal);

    return null;
}




    @Override
    public int getTotalPrice(Long cartId) {
         List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

            // 총합 계산
            return cartItems.stream()
            .mapToInt(item -> item.getQuantity() * item.getProduct().getPrice())
            .sum();
    }









    

    

    
}
