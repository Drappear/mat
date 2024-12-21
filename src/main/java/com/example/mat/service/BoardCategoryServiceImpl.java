package com.example.mat.service;

import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.entity.won.BoardCategory;
import com.example.mat.repository.BoardCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardCategoryServiceImpl implements BoardCategoryService {

    private final BoardCategoryRepository boardCategoryRepository;

    @Override
    public List<BoardCategoryDto> getAllCategories() {
        return boardCategoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BoardCategoryDto getCategoryById(Long categoryId) {
        BoardCategory category = boardCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
        return convertToDto(category);
    }

    private BoardCategoryDto convertToDto(BoardCategory category) {
        return BoardCategoryDto.builder()
                .boardCNo(category.getBoardCNo())
                .name(category.getName())
                .build();
    }
}
