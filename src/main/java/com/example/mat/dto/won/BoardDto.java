package com.example.mat.dto.won;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    private Long bno; // 게시물 번호

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String title; // 게시물 제목

    @NotBlank(message = "내용은 필수 항목입니다.")
    private String content; // 게시물 내용

    @NotBlank(message = "카테고리는 필수 항목입니다.")
    private String boardCategory; // 게시판 카테고리

    private Long viewCount; // 조회수

    private LocalDateTime regDate; // 등록일
    private LocalDateTime updateDate; // 수정일

}
