package com.example.mat.config;

import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardCategory;
import com.example.mat.repository.BoardCategoryRepository;
import com.example.mat.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BoardCategoryRepository boardCategoryRepository;
    private final BoardRepository boardRepository;

    @Override
    public void run(String... args) throws Exception {
        if (boardCategoryRepository.count() == 0) {
            BoardCategory category1 = BoardCategory.builder().name("Free").build();
            BoardCategory category2 = BoardCategory.builder().name("Q&A").build();
            BoardCategory category3 = BoardCategory.builder().name("Tip").build();
            boardCategoryRepository.save(category1);
            boardCategoryRepository.save(category2);
            boardCategoryRepository.save(category3);
        }

        if (boardRepository.count() == 0) {
            BoardCategory freeCategory = boardCategoryRepository.findByName("Free");
            BoardCategory qnaCategory = boardCategoryRepository.findByName("Q&A");
            BoardCategory tipCategory = boardCategoryRepository.findByName("Tip");

            boardRepository.save(Board.builder()
                    .title("Welcome to the Board!")
                    .content("자유게시판")
                    .nick("Admin")
                    .viewCount(0L)
                    .boardCategory(freeCategory)
                    .build());

            boardRepository.save(Board.builder()
                    .title("Announcements")
                    .content("질문게시판")
                    .nick("Admin")
                    .viewCount(0L)
                    .boardCategory(qnaCategory)
                    .build());

            boardRepository.save(Board.builder()
                    .title("Announcements")
                    .content("팁게시판")
                    .nick("Admin")
                    .viewCount(0L)
                    .boardCategory(qnaCategory)
                    .build());
        }
    }
}
