package com.example.mat.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardCategory;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    @Commit
    @Transactional
    @Test
    public void testInsert() {
        // Ensure a category exists for the boards
        BoardCategory category = boardCategoryRepository.findById(1L)
                .orElseGet(() -> boardCategoryRepository.save(BoardCategory.builder()
                        .name("General")
                        .build()));

        // Insert test boards
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Board board = Board.builder()
                    .title("Test Title " + i)
                    .content("This is the content for test board " + i)
                    .nick("User" + i)
                    .viewCount((long) (Math.random() * 100))
                    .boardCategory(category)
                    .build();
            boardRepository.save(board);
        });
    }

    @Test
    public void testPaging() {
        int page = 0; // Page number (0-based index)
        int size = 10; // Number of items per page

        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
        var result = boardRepository.findAll(pageable);

        System.out.println("Total Pages: " + result.getTotalPages());
        System.out.println("Total Elements: " + result.getTotalElements());
        result.getContent().forEach(board -> {
            System.out.println("Board ID: " + board.getBno());
            System.out.println("Title: " + board.getTitle());
            System.out.println("Content: " + board.getContent());
            System.out.println("Nick: " + board.getNick());
            System.out.println("View Count: " + board.getViewCount());
        });
    }
}
