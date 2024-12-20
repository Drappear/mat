package com.example.mat.service;

import com.example.mat.dto.won.BoardCategoryDto;
import java.util.List;

/**
 * BoardCategoryService
 * 게시판 카테고리 관련 비즈니스 로직을 처리합니다.
 */
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

    /**
     * 새로운 카테고리를 추가합니다.
     *
     * @param categoryDto 추가할 카테고리 정보
     * @return 추가된 카테고리 ID
     */
    Long addCategory(BoardCategoryDto categoryDto);
}