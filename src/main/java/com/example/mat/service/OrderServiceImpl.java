package com.example.mat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mat.dto.market.OrderDto;
import com.example.mat.dto.market.OrderItemDto;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Order;
import com.example.mat.entity.market.OrderItem;
import com.example.mat.entity.market.Product;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.CartItemRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.OrderItemRepository;
import com.example.mat.repository.OrderRepository;
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
        private final OrderItemRepository orderItemRepository;
        private final ProductRepository productRepository; // ✅ 상품 업데이트를 위한 추가

        @PersistenceContext
        private EntityManager entityManager;

        @Override
        @Transactional
        public Long createOrder(Long mid, List<Long> selectedCartItemIds, List<Integer> selectedQuantities,
                        String recipientName, String phoneNumber, String email,
                        String zipcode, String addr, String detailAddr) {

                log.info("📌 createOrder() 호출됨 - 회원 ID: {}", mid);

                Member member = memberRepository.findById(mid)
                                .orElseThrow(() -> new EntityNotFoundException("❌ 사용자를 찾을 수 없습니다."));

                List<CartItem> cartItems = cartItemRepository.findByCartItemIds(selectedCartItemIds);
                if (cartItems.isEmpty()) {
                        log.error("❌ 선택된 장바구니 상품이 없습니다.");
                        throw new IllegalStateException("선택된 장바구니 상품이 없습니다.");
                }

                int totalPrice = 0;

                // ✅ 주문 생성 (초기 가격 0)
                Order order = Order.builder()
                                .member(member)
                                .recipientName(recipientName)
                                .phoneNumber(phoneNumber)
                                .email(email)
                                .zipcode(zipcode)
                                .addr(addr)
                                .detailAddr(detailAddr)
                                .orderStatus(com.example.mat.entity.constant.OrderStatus.ORDER)
                                .price(0)
                                .orderUid("ORDER_" + UUID.randomUUID().toString())
                                .orderItems(new ArrayList<>())
                                .build();

                // ✅ Order 먼저 저장 (ID가 필요함)
                order = orderRepository.save(order);

                // ✅ 저장 후 order_uid 업데이트
                order.setOrderUid("ORDER_" + order.getOid());
                orderRepository.save(order);

                // ✅ 주문 항목 처리
                for (int i = 0; i < cartItems.size(); i++) {
                        CartItem cartItem = cartItems.get(i);
                        int orderQuantity = selectedQuantities.get(i);
                        Product product = cartItem.getProduct();

                        int productStock = Integer.parseInt(String.valueOf(product.getQuantity())); // 🚀 숫자로 변환하여 비교

                        if (productStock < orderQuantity) {
                                log.error("❌ 재고 부족 - 상품: {}, 남은 수량: {}, 주문 수량: {}",
                                                product.getName(), productStock, orderQuantity);
                                throw new IllegalStateException("❌ 재고가 부족합니다. 상품명: " + product.getName());
                        }

                        int itemTotalPrice = orderQuantity * product.getPrice();
                        totalPrice += itemTotalPrice;

                        // ✅ 주문 상품 생성
                        OrderItem orderItem = OrderItem.builder()
                                        .order(order)
                                        .product(product)
                                        .quantity(orderQuantity)
                                        .price(product.getPrice())
                                        .build();

                        order.addOrderItem(orderItem);

                        // ✅ 상품 재고 차감
                        product.setQuantity(product.getQuantity() - orderQuantity);
                        productRepository.save(product); // 🔥 상품 정보 업데이트
                        log.info("✅ 상품 재고 업데이트 - 상품: {}, 남은 수량: {}", product.getName(), product.getQuantity());
                }

                // ✅ 최종 주문 가격 설정 후 저장
                order.setPrice(totalPrice);
                orderRepository.save(order);

                log.info("✅ 주문 저장 완료 - 주문 ID: {}, 주문번호(orderUid): {}, 총 금액: {}",
                                order.getOid(), order.getOrderUid(), order.getPrice());

                // // ✅ 주문 완료 후 장바구니에서 해당 상품 삭제
                // cartItemRepository.deleteAll(cartItems);
                // cartItemRepository.flush();
                // entityManager.clear();

                return order.getOid();
        }

        @Transactional
        public OrderDto getOrder(Long orderId) {
                log.info("🔍 주문 데이터 조회 시도 - 주문 ID: {}", orderId);

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

                log.info("✅ 조회된 주문 엔티티: {}", order);

                List<OrderItem> orderItems = orderItemRepository.findItemsByOrderId(orderId);

                if (orderItems.isEmpty()) {
                        log.warn("⚠️ 주문에 포함된 상품이 없습니다. 주문 ID: {}", orderId);
                } else {
                        log.info("✅ 조회된 주문 상품 개수: {}", orderItems.size());
                }

                List<OrderItemDto> orderItemDtos = orderItems.stream()
                                .map(OrderItemDto::fromEntity)
                                .collect(Collectors.toList());

                OrderDto orderDto = OrderDto.builder()
                                .oid(order.getOid())
                                .orderStatus(order.getOrderStatus())
                                .price(order.getPrice())
                                .name(order.getRecipientName())
                                .phoneNumber(order.getPhoneNumber())
                                .email(order.getEmail())
                                .zipcode(order.getZipcode())
                                .addr(order.getAddr())
                                .detailAddr(order.getDetailAddr())
                                .orderItems(orderItemDtos)
                                .build();

                log.info("✅ 변환된 OrderDto: {}", orderDto);

                return orderDto;
        }

        @Override
        public Order getOrderEntity(Long orderId) {
                return orderRepository.findById(orderId)
                                .orElseThrow(() -> new EntityNotFoundException("❌ 주문을 찾을 수 없습니다. 주문 ID: " + orderId));
        }
}
