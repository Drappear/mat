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

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    @PersistenceContext
    private EntityManager entityManager; // ✅ EntityManager 주입

    @Override
    @Transactional
    public Long createOrder(Long mid, List<Long> selectedCartItemIds, List<Integer> selectedQuantities,
            String recipientName, String phoneNumber, String zipcode, String addr, String detailAddr, String email) {

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

        // 주문 고유번호 생성 (아임포트에서 결제 후 검증할 때 필요)
        String orderUid = "ORDER_" + System.currentTimeMillis();

        Order order = Order.builder()
                .member(member)
                .price(totalPrice)
                .quantity(cartItems.size())
                .orderUid(orderUid)
                .name(recipientName)
                .phoneNumber(phoneNumber)
                .zipcode(zipcode)
                .addr(addr)
                .detailAddr(detailAddr)
                .email(email)
                .orderStatus(com.example.mat.entity.constant.OrderStatus.ORDER)
                .build();
        orderRepository.save(order);

        // 선택한 상품만 삭제하도록 수정
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

        return entityToDto(order);
    }

    @Override
    public Order getOrderEntity(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다"));
    }
}
