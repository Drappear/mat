package com.example.mat.dto.won;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 게시판 DTO 클래스
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardDto {

    // 게시글 고유 번호
    private Long bno;

    // 게시글 제목
    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    // 게시글 내용
    @NotBlank(message = "내용은 필수 항목입니다.")
    private String content;

    // 작성자 회원 아이디
    private Long mid;

    // 작성자 닉네임
    private String nickname;

    // 게시글이 속한 카테고리 목록
    @Builder.Default
    private List<BoardCategoryDto> boardCateDtos = new ArrayList<>();

    // 게시글 조회수
    private Long viewCount;

    // 메인 이미지 경로
    private String mainImage;

    // 게시글 작성 시간
    private LocalDateTime regDate;

    // 게시글 수정 시간
    private LocalDateTime updateDate;
}
