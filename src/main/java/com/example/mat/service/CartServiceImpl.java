package com.example.mat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.dto.market.CartItemDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.entity.Member;
import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Product;
import com.example.mat.repository.CartItemRepository;

import com.example.mat.repository.CartRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.ProductRepository;

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

        //Cart 생성   
        Cart cart = cartRepository.findByMember_mid(memberDto.getMid());

        if (cart == null) {
            // 멤버에게 카트 없으면, 카트 생성
           cart = Cart.builder().member(Member.builder().mid(memberDto.getMid()).build()).build();
           cartRepository.save(cart);
        }

        Product product = productRepository.findById(cartItemDto.getPid())
                            .orElseThrow(EntityNotFoundException::new);   

        CartItem savedCartItem = cartItemRepository.findByProductCart(cart, product);

        if(savedCartItem != null){
            // 이미 상품이 있다면 개수를 +
            savedCartItem.addQuantity(cartItemDto.getQuantity());
            cartItemRepository.save(savedCartItem);
            return savedCartItem.getCartItemId();
        } else {
            //CartItem cartItem =  CartItem.createCartItem(cart, product, cartItemDto.getQuantity());
            CartItem cartItem =  CartItem.builder()
            .quantity(cartItemDto.getQuantity())
            .product(product)
            .cart(cart)            
            .build();
            cartItemRepository.save(cartItem);
            return cartItem.getCartItemId();
        }

    }


    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
       
        Cart cart = cartRepository.findByMember_mid(member.getMid());

        if(cart == null){ // 위에서 유저 카트 조회해서, 없으면은 그냥 반환하고
            return cartDetailDtoList;
        }

        cartDetailDtoList 
        = cartItemRepository.findCartDetailDtoList(cart.getCartid());
        return cartDetailDtoList; // 카트 있으면은 cartItemRepository 의 JPQL 쿼리로 걸러진 아이템들을 담영서 반환
    }


    @Override
    public void updateCartItemQuantity(Long cartitemid, int quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCartItemQuantity'");
    }

    @Override
    public void deleteCartItem(Long cartitemid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCartItem'");
    }





    

    

    
}
