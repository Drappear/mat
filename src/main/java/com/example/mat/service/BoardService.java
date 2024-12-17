package com.example.mat.service;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;

import java.util.List;

public interface BoardService {

    Long createPost(BoardDto boardDto);

    void updatePost(Long bno, BoardDto boardDto);

    void deletePost(Long bno);

    BoardDto getPost(Long bno);

    List<BoardDto> getPostList();

    default BoardDto entityToDto(Board board) {
        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .boardCategory(board.getBoardCategory())
                .build();
    }

    default Board dtoToEntity(BoardDto boardDto) {
        return Board.builder()
                .bno(boardDto.getBno())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .viewCount(boardDto.getViewCount())
                .boardCategory(boardDto.getBoardCategory())
                .build();
    }
}
