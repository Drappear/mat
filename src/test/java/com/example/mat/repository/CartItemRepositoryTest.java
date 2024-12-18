package com.example.mat.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mat.entity.Member;
import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.Product;

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

        Member member = memberRepository.findById(21L).get();
        Cart cart = Cart.builder().member(member).build();

        cartRepository.save(cart);  

        
        
    }


    @Test
    public void testGet(){

        Cart cart = cartRepository.findById(23L).get();
        Product product = productRepository.findById(1L).get();        

        System.out.println(cartItemRepository.findByProductCart(cart, product));
        
    }
    
}
