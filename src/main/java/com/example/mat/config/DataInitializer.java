package com.example.mat.config;

import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.service.BoardCategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initBoardCategories(BoardCategoryService boardCategoryService) {
        return args -> {
            if (boardCategoryService.getAllCategories().isEmpty()) {
                boardCategoryService.addCategory(new BoardCategoryDto(null, "Free"));
                boardCategoryService.addCategory(new BoardCategoryDto(null, "Q&A"));
                boardCategoryService.addCategory(new BoardCategoryDto(null, "Tip"));
                System.out.println("Default categories initialized.");
            }
        };
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 86c8cc0c6022911626db3c215fc316a3a0f5ded7
