package com.example.mat.repository;

import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 게시판 이미지 Repository 인터페이스
 * Spring Data JPA를 사용하여 데이터베이스와 상호작용합니다.
 */
@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    /**
     * 게시글에 연관된 이미지를 삭제합니다.
     *
     * @param board 삭제 대상 게시글
     */
    void deleteByBoard(Board board);

    /**
     * 게시글에 연관된 이미지를 조회합니다.
     *
     * @param board 조회 대상 게시글
     * @return 연관된 이미지
     */
    BoardImage findByBoard(Board board);
}
