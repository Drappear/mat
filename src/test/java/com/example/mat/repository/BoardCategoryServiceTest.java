package com.example.mat.repository;

import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.service.BoardCategoryService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BoardCategoryServiceTest {

    @Autowired
    private BoardCategoryService boardCategoryService;

    @Test
    public void testGetAllCategories() {
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();

        if (categories.isEmpty()) {
            System.out.println("카테고리 데이터가 비어 있습니다!");
        } else {
            System.out.println("카테고리 목록:");
            categories.forEach(category -> System.out.println(category.getName()));
        }
    }
}
