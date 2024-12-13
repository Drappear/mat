package com.example.mat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.shin.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserid(String userid);

    Optional<Member> findByEmail(String email); // 이메일 중복 확인용

    boolean existsByUserid(String userid); // 사용자 ID 중복 여부 확인

    boolean existsByNickname(String nickname); // 사용자 닉네임 중복 여부 확인

    // boolean existsByEmail(String email); // 이메일 중복 여부 확인
}
