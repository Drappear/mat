package com.example.mat.repository.shin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mat.entity.shin.Member;
import com.example.mat.entity.shin.MemberImage;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    /**
     * 게시글에 연관된 이미지를 삭제합니다.
     *
     * @param board 삭제 대상 게시글
     */
    // @Modifying
    // @Query("DELETE FROM MemberImage mi WHERE mi.member = :member")
    void deleteByMember(Member member);

    /**
     * 게시글에 연관된 이미지를 조회합니다.
     *
     * @param board 조회 대상 게시글
     * @return 연관된 이미지
     */
    MemberImage findByMember(Member member);

    /**
     * UUID 중복 여부 확인
     *
     * @param uuid 중복 확인할 UUID
     * @return UUID가 존재하면 true, 존재하지 않으면 false
     */
    boolean existsByUuid(String uuid);
}