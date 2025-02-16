package com.example.mat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Order;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.CartItemRepository;
import com.example.mat.repository.CartRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    @Commit
    public void createOrder() {

        // 1. 멤버 조회
        Member member = memberRepository.findById(51L)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자를 찾을 수 없습니다."));

        // 2. 장바구니 조회 (영속성 보장)
        Cart cart = cartRepository.findByMember(member);
        cart = cartRepository.findById(cart.getCartid()).orElseThrow();

        // 3. 장바구니 아이템 목록 조회 (주문 가격 및 개수 검증용)
        List<CartItem> cartItems = cartItemRepository.findByCartItem(cart);

        // 4. 삭제 전 장바구니 아이템 개수 확인
        System.out.println("Cart 객체 정보: " + cart);
        System.out.println("Cart ID: " + cart.getCartid());
        int cartItemCountBefore = cartItemRepository.countByCartItem(cart);
        System.out.println("삭제 전 장바구니 아이템 개수 (DB 조회): " + cartItemCountBefore);

        // 5. 주문 생성
        Long orderId = orderService.createOrder(member.getMid());

        // 6. 주문이 정상적으로 생성되었는지 확인
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        assertTrue(orderOptional.isPresent(), "주문이 정상적으로 생성되지 않았습니다.");
        Order order = orderOptional.get();

        // 7. 주문 총 가격 검증 (장바구니 아이템 가격 합산)
        int expectedTotalPrice = cartItems.stream()
                .mapToInt(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        assertEquals(expectedTotalPrice, order.getPrice(), "주문 총 가격이 올바르지 않습니다.");
        System.out.println("주문 총 가격 검증 완료! expectedTotalPrice: " + expectedTotalPrice);
        System.out.println("order.getPrice(): " + order.getPrice());

        // 8. 주문 상품 개수 검증 (장바구니에 있던 개수와 일치해야 함)
        assertEquals(cartItems.size(), order.getQuantity(), "주문 상품 수량이 올바르지 않습니다.");
        System.out.println("주문 상품 개수 검증 완료! cartItems.size(): " + cartItems.size());
        System.out.println("order.getQuantity(): " + order.getQuantity());

        // 9. 장바구니 비우기
        cartItemRepository.deleteAll(cartItems);
        cartItemRepository.flush();
        entityManager.clear();

        // 10. 삭제 후 장바구니 아이템 개수 확인
        int remainingCount = cartItemRepository.countByCartItem(cart);
        System.out.println("삭제 후 장바구니 아이템 개수 (DB 조회): " + remainingCount);
        assertTrue(remainingCount == 0, "장바구니가 비워지지 않았습니다!");
        System.out.println("장바구니 비우기 검증 완료!");
    }

}
