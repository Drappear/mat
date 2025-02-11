package com.example.mat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.dto.market.CartItemDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Product;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.CartItemRepository;
import com.example.mat.repository.CartItemRepositoryTest;
import com.example.mat.repository.CartRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@Transactional
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
    
    @Autowired
    CartRepository cartRepository;


    // 카트 담기 테스트
    @Transactional
    @Test
    public void addCart(){
        //  새로운 CartItem을 장바구니에 추가하는 경우 > 상품과 수량이 올바르게 저장되었는지 확인
        Product product = productRepository.findById(1L).get();
        //Member member = memberRepository.findById(51L).get();

        // 카트에 담을 상품, 수량을 cartItemDto 객체에 넣기
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(2);
        cartItemDto.setPid(product.getPid());

        MemberDto memberDto = new MemberDto();
        memberDto.setMid(51L);

        // 생성된 카트 상품 아이디를 cartitemid 변수에 저장장
        Long cartitemid = cartService.addCart(cartItemDto, memberDto);
        // 카트 상품 아이디를 이용해 생성된 카트 상품 정보 조회회
        CartItem cartItem = cartItemRepository.findById(cartitemid).orElse(null);
        assertNotNull(cartItem);
        assertEquals(2, cartItem.getQuantity());
        assertEquals(product.getPid(), cartItem.getProduct().getPid());

        // 상품 아이디와 카트에 저장된 상품 아이디가 같다면 테스트 성공
        assertEquals(product.getPid(), cartItem.getProduct().getPid());
        // 카트에 담았던 수량과 실제로 카트에 저장된 수량이 같다면 테스트 성공
        assertEquals(cartItemDto.getQuantity(), cartItem.getQuantity());
    }

    @Transactional
    @Test
    public void ExceedsStock(){
         // 상품 수량 초과 테스트
    // Product product = productRepository.findById(15L).get();

    // CartItemDto cartItemDto = new CartItemDto();
    // cartItemDto.setPid(product.getPid());
    // cartItemDto.setQuantity(15); // 재고보다 많은 수량 요청

    // MemberDto memberDto = new MemberDto();
    // memberDto.setMid(51L);

    // Exception exception = assertThrows(IllegalStateException.class, () -> {
    //     cartService.addCart(cartItemDto, memberDto);
    // });
    // System.out.println("수량 초과");
    // assertEquals("구매 가능한 수량을 초과하였습니다", exception.getMessage());


        Product product = Product.builder().name("Test Product").quantity(10).build();
        productRepository.save(product);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setPid(product.getPid());
        cartItemDto.setQuantity(15); // 재고보다 많은 수량 요청
        System.out.println("Test Debug: Saved Product ID: " + product.getPid());
    System.out.println("Test Debug: Saved Product Quantity: " + product.getQuantity());

        MemberDto memberDto = new MemberDto();
        memberDto.setMid(51L);

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cartService.addCart(cartItemDto, memberDto);
        });

        // Log and verify exception message
        System.out.println("수량 초과 예외 발생: " + exception.getMessage());
        assertEquals("구매 가능한 수량을 초과하였습니다", exception.getMessage());
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

    @Test
    public void getcartList(){

        List<CartDetailDto> cartList = cartService.getCartList(51L);
        cartList.forEach(item -> System.out.println(item));


    }


    @Test
    @Rollback(false)
    public void testRemove(){
       
       cartService.deleteCartItem(82L);
    }

    @Test
    public void testUpdateCartItemQuantity() {
        cartItemRepository.deleteAll();

        // Given
        Product product = Product.builder().name("Test Product").price(5000).quantity(100).build();
        productRepository.save(product);
        

        MemberDto memberDto = MemberDto.builder().mid(51L).build();
        Cart cart = cartRepository.findByMember(Member.builder().mid(51L).build());

        if (cart == null) {
            cart = Cart.builder().member(Member.builder().mid(51L).build()).build();
            cartRepository.save(cart);
        }

        CartItem cartItem = CartItem.builder().cart(cart).product(product).quantity(5).build();
        cartItemRepository.save(cartItem);

        // When
        Map<String, Integer> updatedValues = cartService.updateCartItemQuantity(cartItem.getCartItemId(), 10);

        // Then
        assertEquals(50000, updatedValues.get("updatedItemTotal")); // 상품 총 가격 = 10 * 5000
        assertEquals(50000, updatedValues.get("updatedCartTotal")); // 카트 총 가격 = 10 * 5000

        CartItem updatedCartItem = cartItemRepository.findById(cartItem.getCartItemId()).orElseThrow();
        assertEquals(10, updatedCartItem.getQuantity());
    }

    
}
