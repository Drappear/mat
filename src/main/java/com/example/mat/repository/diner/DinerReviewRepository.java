package com.example.mat.repository.diner;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerReview;
import com.example.mat.entity.shin.Member;

public interface DinerReviewRepository extends JpaRepository<DinerReview, Long> {
    // 리뷰 가져오기
    @EntityGraph(attributePaths = "member", type = EntityGraphType.FETCH)
    List<DinerReview> findByDiner(Diner diner);

    @Modifying
    @Query("DELETE FROM DinerReview dr WHERE dr.diner = :diner")
    void deleteByDiner(Diner diner);

    @Modifying
    @Query("DELETE FROM DinerReview dr WHERE dr.member =:member")
    void deleteByMember(Member member);
}
