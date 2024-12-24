package com.example.mat.config;

import com.example.mat.entity.won.BoardCategory;
import com.example.mat.repository.BoardCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BoardCategoryRepository boardCategoryRepository;

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

    }
}
