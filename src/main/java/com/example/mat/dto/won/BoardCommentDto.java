package com.example.mat.dto.won;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDto {
    private Long id;
    private String content;
    private String userid;
    private Long boardId;
    // private Long parentId;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    // private List<BoardCommentDto> replies; // 대댓글 리스트
}