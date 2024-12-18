package com.example.mat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isNull;

import java.util.stream.IntStream;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mat.dto.market.CartItemDto;
import com.example.mat.entity.Member;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Product;
import com.example.mat.repository.CartItemRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartItemRepository cartItemRepository;


    // 카트 담기 테스트
    @Transactional
    @Test
    public void addCart(){
        Product product = productRepository.findById(1L).get();
        Member member = memberRepository.findById(21L).get();

        // 카트에 담을 상품, 수량을 cartItemDto 객체에 넣기
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(3);
        cartItemDto.setCartitemid(product.getPid());

        // 생성된 카트 상품 아이디를 cartitemid 변수에 저장장
        Long cartitemid = cartService.addCart(cartItemDto, member.getEmail());
        // 카트 상품 아이디를 이용해 생성된 카트 상품 정보 조회회
        CartItem cartItem = cartItemRepository.findById(cartitemid)
                .orElseThrow(EntityNotFoundException::new);

        // 상품 아이디와 카트에 저장된 상품 아이디가 같다면 테스트 성공
        assertEquals(product.getPid(), cartItem.getProduct().getPid());
        // 카트에 담았던 수량과 실제로 카트에 저장된 수량이 같다면 테스트 성공
        assertEquals(cartItemDto.getQuantity(), cartItem.getQuantity());
    }


    @Test
    public void insertMember(){
        IntStream.rangeClosed(1, 10).forEach(i ->{

            Member member = Member.builder()
            .userid("userid" + i)
            .password("password" +i)
            .email("user"+i+"@gmail.com")
            .username("username"+i)
            .nickname("nickname"+i)
            .tel("01012345678")
            .build();

            memberRepository.save(member);

        });
    }

    
}
