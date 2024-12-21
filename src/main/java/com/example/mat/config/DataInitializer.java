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
            BoardCategory category1 = BoardCategory.builder().name("General").build();
            BoardCategory category2 = BoardCategory.builder().name("Announcements").build();
            boardCategoryRepository.save(category1);
            boardCategoryRepository.save(category2);
        }

        if (boardRepository.count() == 0) {
            BoardCategory generalCategory = boardCategoryRepository.findByName("General");
            BoardCategory announcementCategory = boardCategoryRepository.findByName("Announcements");

            boardRepository.save(Board.builder()
                    .title("Welcome to the Board!")
                    .content("This is the first post in the General category.")
                    .nick("Admin")
                    .viewCount(0L) // 명시적으로 설정
                    .boardCategory(generalCategory)
                    .build());

            boardRepository.save(Board.builder()
                    .title("Announcements")
                    .content("This is the first announcement.")
                    .nick("Admin")
                    .viewCount(0L) // 명시적으로 설정
                    .boardCategory(announcementCategory)
                    .build());
        }
    }
}
