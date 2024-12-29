package com.example.mat.repository;

import com.example.mat.entity.won.BoardComment;
import com.example.mat.entity.won.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    /**
     * 특정 게시글의 모든 댓글 조회 (최신 순)
     * 부모 댓글과 대댓글을 포함하여 등록일 기준 내림차순으로 정렬합니다.
     *
     * @param board 댓글이 속한 게시글
     * @return 댓글 목록
     */
    List<BoardComment> findByBoardOrderByRegDateDesc(Board board);

    /**
     * 특정 게시글의 부모 댓글 조회 (최신 순)
     * 부모 댓글만 가져오며, 등록일 기준 내림차순으로 정렬합니다.
     *
     * @param board 댓글이 속한 게시글
     * @return 부모 댓글 목록
     */
    List<BoardComment> findByBoardAndParentIsNullOrderByRegDateDesc(Board board);

    /**
     * 특정 부모 댓글의 대댓글 조회 (오름차순)
     * 대댓글을 부모-자식 관계로 조회하며, 등록일 기준 오름차순으로 정렬합니다.
     *
     * @param parent 부모 댓글
     * @return 대댓글 목록
     */
    List<BoardComment> findByParentOrderByRegDateAsc(BoardComment parent);

    /**
     * 특정 부모 댓글의 대댓글 개수 조회
     *
     * @param parent 부모 댓글
     * @return 대댓글 개수
     */
    long countByParent(BoardComment parent);
}
