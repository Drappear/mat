package com.example.mat.dto.won;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 게시판 카테고리 DTO 클래스

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardCategoryDto {

<<<<<<< HEAD
    // 카테고리 고유 번호
    private Long boardCNo;

    // 카테고리 이름
    @NotBlank(message = "카테고리 선택은 필수 항목입니다.")
    private String name;
=======
  // 카테고리 고유 번호
  private Long boardCNo;

  // 카테고리 이름
  @NotBlank(message = "카테고리 선택은 필수 항목입니다.")
  private String name;
>>>>>>> 86c8cc0c6022911626db3c215fc316a3a0f5ded7
}