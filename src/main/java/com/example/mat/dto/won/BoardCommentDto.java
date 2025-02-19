package com.example.mat.dto.won;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDto {
    private Long id;
    private String content;
    private String userid;
    private Long boardId;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private String profileImage; // 추가: 댓글 작성자의 프로필 이미지 URL
}
