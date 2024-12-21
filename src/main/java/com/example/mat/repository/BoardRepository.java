package com.example.mat.repository;

import com.example.mat.entity.won.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b.bno, b.title, b.viewCount, b.regDate, b.nickname " +
            "FROM Board b " +
            "ORDER BY b.regDate DESC")
    Page<Object[]> getListPage(Pageable pageable);

<<<<<<< HEAD
}
=======
}
>>>>>>> 86c8cc0c6022911626db3c215fc316a3a0f5ded7
