package com.example.mat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.mat.dto.market.OrderDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Order;
import com.example.mat.entity.market.Payment;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.CartItemRepository;
import com.example.mat.repository.CartRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.OrderRepository;
import com.example.mat.repository.PaymentRepository;
import com.example.mat.repository.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    @PersistenceContext
    private EntityManager entityManager; // ✅ EntityManager 주입

    @Override
    @Transactional
    public Long createOrder(Long mid, List<Long> selectedCartItemIds, List<Integer> selectedQuantities) {
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 선택한 상품 ID만 가져오기
        List<CartItem> cartItems = cartItemRepository.findByCartItemIds(selectedCartItemIds);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("선택된 장바구니 상품이 없습니다.");
        }

        int totalPrice = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);
            cartItem.setQuantity(selectedQuantities.get(i)); // 선택된 수량으로 업데이트
            totalPrice += cartItem.getQuantity() * cartItem.getProduct().getPrice();
        }

        Order order = Order.builder()
                .member(member)
                .price(totalPrice)
                .quantity(cartItems.size())
                .orderStatus(com.example.mat.entity.constant.OrderStatus.ORDER)
                .build();
        orderRepository.save(order);

        Payment payment = Payment.builder()
                .member(member)
                .order(order)
                .price(totalPrice)
                .totalPrice((long) totalPrice)
                .status(true)
                .build();
        paymentRepository.save(payment);

        // ✅ 선택한 상품만 삭제하도록 수정
        cartItemRepository.deleteAll(cartItems);
        cartItemRepository.flush();
        entityManager.clear();

        log.info("주문 완료! 주문 ID: {}", order.getOid());
        return order.getOid();
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        // 주문 정보 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        return OrderDto.builder()
                .oid(order.getOid())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
