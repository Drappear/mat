package com.example.mat.repository;

import java.lang.reflect.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mat.entity.shin.MemberImage;
import com.example.mat.repository.shin.MemberImageRepository;

@Repository
public interface ProfileImageRepository extends JpaRepository<MemberImage, Long>, MemberImageRepository {
    // movie_mno 를 이용해 movie_image 제거 메서드 생성
    @Modifying
    @Query("DELETE FROM MemberImage mi WHERE mi.member = :member")
    void deleteByMember(Member member);
}
