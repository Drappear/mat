package com.example.mat.service;

import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.entity.won.BoardCategory;
import com.example.mat.repository.BoardCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BoardCategoryServiceImpl
 * 게시판 카테고리 관련 비즈니스 로직을 구현합니다.
 */
@Service
@RequiredArgsConstructor
public class BoardCategoryServiceImpl implements BoardCategoryService {

    private final BoardCategoryRepository boardCategoryRepository;

    /**
     * 모든 게시판 카테고리를 조회합니다.
     *
     * @return 카테고리 DTO 리스트
     */
    @Override
    public List<BoardCategoryDto> getAllCategories() {
        List<BoardCategory> categories = boardCategoryRepository.findAll();
        return categories.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    /**
     * 특정 카테고리를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 DTO
     */
    @Override
    public BoardCategoryDto getCategoryById(Long categoryId) {
        BoardCategory category = boardCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
        return entityToDto(category);
    }

    /**
     * 새로운 카테고리를 추가합니다.
     *
     * @param categoryDto 추가할 카테고리 정보
     * @return 추가된 카테고리 ID
     */
    @Override
    public Long addCategory(BoardCategoryDto categoryDto) {
        BoardCategory category = BoardCategory.builder()
                .name(categoryDto.getName())
                .build();
        BoardCategory savedCategory = boardCategoryRepository.save(category);
        return savedCategory.getBoardCNo();
    }

    /**
     * BoardCategory 엔티티를 DTO로 변환합니다.
     *
     * @param category BoardCategory 엔티티
     * @return BoardCategoryDto
     */
    private BoardCategoryDto entityToDto(BoardCategory category) {
        return BoardCategoryDto.builder()
                .boardCNo(category.getBoardCNo())
                .name(category.getName())
                .build();
    }
}
