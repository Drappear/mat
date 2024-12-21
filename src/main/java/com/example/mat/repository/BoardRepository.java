package com.example.mat.repository;

import com.example.mat.entity.won.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

        @Query("SELECT b.bno, b.title, b.viewCount, b.regDate, b.nick " +
                        "FROM Board b " +
                        "ORDER BY b.regDate DESC")
        Page<Object[]> getListPage(Pageable pageable);

        @Query("SELECT b " +
                        "FROM Board b " +
                        "WHERE (:keyword IS NULL OR b.title LIKE %:keyword%) " +
                        "AND (:category IS NULL OR b.boardCategory.boardCNo = :category) " +
                        "ORDER BY b.regDate DESC")
        Page<Board> findByKeywordAndCategory(String keyword, Long category, Pageable pageable);
}
