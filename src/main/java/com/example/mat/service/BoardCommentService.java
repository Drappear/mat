package com.example.mat.service;

import com.example.mat.dto.won.BoardCommentDto;
import java.util.List;

public interface BoardCommentService {
    Long addComment(BoardCommentDto boardCommentDto);

    void updateComment(Long id, String content);

    void deleteComment(Long id);

    List<BoardCommentDto> getCommentsByBoard(Long boardId);
}