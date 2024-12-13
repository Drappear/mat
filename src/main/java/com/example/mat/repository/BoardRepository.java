package com.example.mat.repository;

import com.example.mat.entity.won.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 특정 카테고리의 게시물 목록 조회
    List<Board> findByBoardCategory(String boardCategory);

    // 제목으로 게시물 검색
    List<Board> findByTitleContaining(String keyword);

    // 게시물 조회수가 높은 순으로 정렬하여 조회
    List<Board> findAllByOrderByViewCountDesc();
}