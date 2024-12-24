package com.example.mat.service;

import com.example.mat.dto.won.BoardCategoryDto;
import java.util.List;

public interface BoardCategoryService {

    /**
     * 모든 게시판 카테고리를 조회합니다.
     *
     * @return 카테고리 DTO 리스트
     */
    List<BoardCategoryDto> getAllCategories();

    /**
     * 특정 카테고리를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 DTO
     */
    BoardCategoryDto getCategoryById(Long categoryId);
}
