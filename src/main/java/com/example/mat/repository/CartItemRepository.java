package com.example.mat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.CartItem;
import com.example.mat.entity.market.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

    //카드 아이디와 상품 아이디로 상품이 장바구니에 들어있는지 조회
   //CartItem findByCart_cartidAndProduct_pid(Long cartid, Long pid);


   @Query("select ci from CartItem ci where ci.cart = :cart and ci.product = :product")
   CartItem findByProductCart(Cart cart, Product product);

  

   // 장바구니 페이지에 전달할 CartDetailDto 를
   // 쿼리로 조회하여 CartDetailList에 담아줌
//    @Query("select new com.example.mat.dto.market.CartDetailDto(ci.cartitemid, p.name, p.price, ci.quantity) " +
//    "from CartItem ci " +
//    "join ci.product p " +
//    "where ci.cart.cartid = :cartid " 
//    )   
//    List<CartDetailDto> findCartDetailDtoList(Long cartid);

    
}
 