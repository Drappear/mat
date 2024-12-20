package com.example.mat.repository;

import com.example.mat.entity.won.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 게시판 Repository 인터페이스
 * Spring Data JPA를 사용하여 데이터베이스와 상호작용합니다.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * 페이징 처리된 게시글 리스트를 가져오는 쿼리.
     * 게시글 정보, 작성자 이름, 게시글 작성일 등을 포함하여 반환합니다.
     * 
     * @param pageable 페이징 정보를 담고 있는 객체
     * @return 페이징 처리된 게시글 리스트
     */

    @Query("SELECT b.bno, b.title, b.viewCount, b.regDate, b.nickname " +
            "FROM Board b " +
            "ORDER BY b.regDate DESC")
    // @Query("SELECT b.bno, b.title, b.viewCount, b.regDate, m.nickname " +
    // "FROM Board b " +
    // "LEFT JOIN b.member m " +
    // "ORDER BY b.regDate DESC")
    Page<Object[]> getListPage(Pageable pageable);
}
