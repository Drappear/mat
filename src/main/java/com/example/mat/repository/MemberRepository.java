package com.example.mat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mat.dto.shin.MemberImageDto;
import com.example.mat.entity.shin.Member;

import jakarta.transaction.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserid(String userid);

    Optional<Member> findByEmail(String email); // 이메일 중복 확인용

    boolean existsByUserid(String userid); // 사용자 ID 중복 여부 확인

    boolean existsByNickname(String nickname); // 사용자 닉네임 중복 여부 확인

    // boolean existsByEmail(String email); // 이메일 중복 여부 확인

    // 닉네임 수정
    @Modifying
    @Query("UPDATE Member m SET m.nickname=:nickname WHERE m.userid=:userid")
    void updateNickname(String nickname, String userid);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.email = :email, m.addr = :addr, m.detailAddr = :detailAddr, m.tel = :tel WHERE m.userid = :userid")
    void updatePersonalInfo(String email, String addr, String detailAddr, String tel, String userid);

    @Modifying
    @Query("UPDATE MemberImage mi SET mi.uuid = :uuid, mi.imgName = :imgName, mi.path = :path WHERE mi.member.id = :memberId")
    int updateProfileImage(@Param("memberId") Long memberId, @Param("uuid") String uuid,
            @Param("imgName") String imgName, @Param("path") String path);
}
