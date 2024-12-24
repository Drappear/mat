package com.example.mat.repository.shin;

import java.lang.reflect.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mat.entity.shin.MemberImage;
import java.util.List;

/**
 * 게시판 이미지 Repository 인터페이스
 * Spring Data JPA를 사용하여 데이터베이스와 상호작용합니다.
 */
@Repository
public interface ProfileImageRepository extends JpaRepository<MemberImage, Long> {

    /**
     * 게시글에 연관된 이미지를 조회합니다.
     *
     * @param
     * @return 연관된 이미지
     */
    List<MemberImage> findByMember(Member member);

    /**
     * UUID 중복 여부 확인
     *
     * @param uuid 중복 확인할 UUID
     * @return UUID가 존재하면 true, 존재하지 않으면 false
     */
    boolean existsByUuid(String uuid);
}
