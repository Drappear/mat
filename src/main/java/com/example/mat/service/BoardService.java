package com.example.mat.service;

import java.util.List;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;

public interface BoardService {
    // 게시물 등록
    Long createPost(BoardDto boardDto);

    // 게시물 수정
    void updatePost(Long bno, BoardDto boardDto);

    // 게시물 삭제
    void deletePost(Long bno);

    // 게시물 단일 조회
    BoardDto getPost(Long bno);

    // 게시물 목록 조회
    List<BoardDto> getPostList();

    // DTO를 Entity로 변환
    default BoardDto entityToDto(Board board) {
        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .boardCategory(board.getBoardCategory())
                .build();
    }

    // Entity를 DTO로 변환
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
