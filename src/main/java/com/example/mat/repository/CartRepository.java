package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mat.entity.market.Cart;
import java.util.List;
import com.example.mat.entity.shin.Member;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // 현재 로그인한 멤버의 카트 찾기
    Cart findByMember(Member member);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.member =:member")
    void deleteByMember(Member member);
}
