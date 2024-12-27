package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.mat.entity.market.Cart;
import java.util.List;
import com.example.mat.entity.shin.Member;



public interface CartRepository extends JpaRepository<Cart, Long>{
    // 현재 로그인한 멤버의 카트 찾기
    Cart findByMember(Member member);
    
}
