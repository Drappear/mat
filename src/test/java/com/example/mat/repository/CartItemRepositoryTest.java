package com.example.mat.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Product;
import com.example.mat.entity.shin.Member;

import jakarta.transaction.Transactional;

@SpringBootTest
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void testInsert(){

        Member member = memberRepository.findById(51L).get();
        Cart cart = cartRepository.findByMember(member);

        CartItem cartItem = cartItemRepository.findByProductCart(cart.getCartid(),19L);

        System.out.println(cartItem);     
        
    }


    @Transactional
    @Test
    public void testGet(){

        Cart cart = cartRepository.findById(41L).get();        
        
        cartItemRepository.findByCartItem(cart).forEach(item ->{
            System.out.println(item);
        });

      
        
    }


    @Test
    public void testDelete(){

        cartItemRepository.deleteById(43L);
        
    }
    
}
