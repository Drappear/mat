package com.example.mat.service;

import java.util.ArrayList;
import java.util.List;
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

        if (savedCartItem != null) {
            // 카트에 이미 상품이 있다면 기존 수량과 request 수량을 +
            int totalQuantity = savedCartItem.getQuantity() + requestedQuantity;
            if (totalQuantity > productQuantity) {
                throw new IllegalStateException("구매 가능한 수량을 초과하였습니다");
            }

            // 수량 업데이트
            savedCartItem.addQuantity(cartItemDto.getQuantity());
            cartItemRepository.save(savedCartItem);
            return savedCartItem.getCartItemId();

        } else {

            if (requestedQuantity > productQuantity) {
                throw new IllegalStateException("구매 가능한 수량을 초과하였습니다");
            }

            // 새로운 CartItem 생성
            CartItem cartItem = CartItem.builder()
                    .quantity(cartItemDto.getQuantity())
                    .product(product)
                    .cart(cart)
                    .build();
            cartItemRepository.save(cartItem);
            return cartItem.getCartItemId();
        }

    }

    public List<CartDetailDto> getCartList(Long mid) {

        List<CartDetailDto> cartDetailDtoList;

        // 로그인 정보로 member 찾기기
        Member member = memberRepository.findById(mid).get();
        Cart cart = cartRepository.findByMember(member);

        if (cart == null) { // 위에서 유저 카트 조회해서, 없으면은 그냥 반환
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

        // return cartDetailDtoList;
        return cartDetailDtoList;
    }

    @Override
    public void deleteCartItem(Long cartitemid) {

        cartItemRepository.deleteById(cartitemid);

    }

    @Override
    public int getTotalPrice(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        // 총합 계산
        return cartItems.stream()
                .mapToInt(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
    }

    @Override
    public List<CartDetailDto> getSelectedCartItems(Long memberId, List<Long> cartItemIds) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Cart cart = cartRepository.findByMember(member);
        if (cart == null) {
            return new ArrayList<>();
        }

        List<CartItem> selectedCartItems = cartItemRepository.findByCartItemIds(cartItemIds);

        return entityToDto(selectedCartItems);
    }

}
