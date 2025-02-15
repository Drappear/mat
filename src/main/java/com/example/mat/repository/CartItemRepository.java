package com.example.mat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.CartItem;

import jakarta.transaction.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

   // 장바구니 목록
   @Query("select ci from CartItem ci where ci.cart = :cart")
   List<CartItem> findByCartItem(Cart cart);

   // 기존 카트에 동일한 상품이 존재 시 수량 추가 메소드 필요
   @Query("select ci from CartItem ci where ci.cart.cartid = :cartid and ci.product.pid = :pid")
   CartItem findByProductCart(@Param("cartid") Long cartid, @Param("pid") Long pid);

   @Query("select ci from CartItem ci where ci.cart.cartid = :cartid")
   List<CartItem> findByCartId(@Param("cartid") Long cartid);

   @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart = :cart")
   int countByCartItem(@Param("cart") Cart cart);

   // 선택된 상품들만 조회
   @Query("SELECT ci FROM CartItem ci WHERE ci.cartItemId IN :cartItemIds")
   List<CartItem> findByCartItemIds(@Param("cartItemIds") List<Long> cartItemIds);

}
