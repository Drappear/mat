package com.example.mat.service;

import com.example.mat.dto.won.BoardCommentDto;
import java.util.List;

public interface BoardCommentService {
    Long addComment(BoardCommentDto boardCommentDto);

    void updateComment(Long bcid, String content);

    void deleteComment(Long bcid);

    List<BoardCommentDto> getCommentsByBoard(Long boardId);
}
