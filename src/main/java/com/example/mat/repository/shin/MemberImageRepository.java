package com.example.mat.repository.shin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mat.entity.shin.MemberImage;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    // 특정 회원의 모든 이미지 조회
    List<MemberImage> findByMemberMid(Long memberId);

    // 특정 UUID로 이미지 조회
    Optional<MemberImage> findByUuid(String uuid);

    // 특정 회원의 프로필 이미지 삭제
    void deleteByMemberMid(Long memberId);
}