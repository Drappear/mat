package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
=======
import org.springframework.stereotype.Repository;
>>>>>>> 5483c4b598ad365e84282cfc2861d229dc5ddbb5

import com.example.mat.entity.market.Cart;
import java.util.List;
import com.example.mat.entity.shin.Member;

<<<<<<< HEAD
public interface CartRepository extends JpaRepository<Cart, Long> {
=======

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
>>>>>>> 5483c4b598ad365e84282cfc2861d229dc5ddbb5
    // 현재 로그인한 멤버의 카트 찾기
    Cart findByMember(Member member);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.member =:member")
    void deleteByMember(Member member);
}
