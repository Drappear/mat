package com.example.mat.repository;

import com.example.mat.entity.shin.Member;
import com.example.mat.entity.shin.MemberImage;
import com.example.mat.entity.won.Board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

        @Query("SELECT b.bno, b.title, b.viewCount, b.regDate, b.member.mid " +
                        "FROM Board b " +
                        "JOIN b.member m " +
                        "ORDER BY b.regDate DESC")
        Page<Object[]> getListPage(Pageable pageable);

        @Query("SELECT b " +
                        "FROM Board b " +
                        "WHERE (:keyword IS NULL OR b.title LIKE %:keyword%) " +
                        "AND (:category IS NULL OR b.boardCategory.boardCNo = :category) " +
                        "ORDER BY b.regDate DESC")
        Page<Board> findByKeywordAndCategory(String keyword, Long category, Pageable pageable);

        @Query("SELECT b FROM Board b WHERE b.member.userid = :userid ORDER BY b.regDate DESC")
        Page<Board> findByUserid(String userid, Pageable pageable);

        @Modifying
        @Query("DELETE FROM Board b WHERE b.member =:member")
        void deleteByMember(Member member);
}
