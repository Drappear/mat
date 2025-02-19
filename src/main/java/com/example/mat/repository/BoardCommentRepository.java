package com.example.mat.repository;

import com.example.mat.entity.won.BoardComment;
import com.example.mat.entity.won.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    /**
     * 특정 게시글의 댓글 목록 조회 (최신 순)
     * 
     * @param board 댓글이 속한 게시글
     * @return 댓글 목록
     */
    List<BoardComment> findByBoardOrderByRegDateDesc(Board board);

    /**
     * 특정 게시글의 댓글 개수 조회
     * 
     * @param boardId 댓글 개수를 조회할 게시글 ID
     * @return 댓글 개수
     */
    @Query("SELECT COUNT(c) FROM BoardComment c WHERE c.board.bno = :boardId")
    Long countByBoardId(@Param("boardId") Long boardId);
}
