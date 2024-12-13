package com.example.mat.repository;

import com.example.mat.entity.won.Board;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.stream.LongStream;

@SpringBootTest // Spring Boot의 전체 컨텍스트를 로드
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository; // 테스트할 BoardRepository

    @Commit
    @Transactional
    @Test
    public void testSavePost() {

        LongStream.rangeClosed(1, 5).forEach(i -> {
            // Board 엔티티 객체를 생성
            Board board = Board.builder()
                    .title("title" + i) // 제목 설정
                    .content("content" + i) // 내용 설정
                    .viewCount(0L) // 조회수 초기값 설정 (기본값 0)
                    .boardCategory("cate" + i) // 카테고리 설정
                    .build();
            boardRepository.save(board); // Board 엔티티 저장
        });

    }

}
