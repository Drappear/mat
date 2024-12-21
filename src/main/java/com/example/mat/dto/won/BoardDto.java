package com.example.mat.dto.won;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시판 DTO 클래스
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardDto {

    private Long bno;

    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 항목입니다.")
    private String content;

    private String nickname;
    private Long viewCount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private Long categoryId; // 카테고리 ID를 사용

    private String imageFileName;
<<<<<<< HEAD
}
=======
}
>>>>>>> 86c8cc0c6022911626db3c215fc316a3a0f5ded7
