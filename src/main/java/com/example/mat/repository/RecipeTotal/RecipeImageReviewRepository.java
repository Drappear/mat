package com.example.mat.repository.RecipeTotal;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeImageReviewRepository {

  // 페이지 나누기 + 검색
  Page<Object[]> getTotalList(String type, String keyword, Pageable pageable);

  // 특정 recipe 정보 조회
  List<Object[]> getRecipeRow(Long rno);

}