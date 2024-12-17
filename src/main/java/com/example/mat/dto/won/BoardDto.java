package com.example.mat.dto.won;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @NotBlank(message = "카테고리는 필수 항목입니다.")
    private String boardCategory;

    private Long viewCount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
