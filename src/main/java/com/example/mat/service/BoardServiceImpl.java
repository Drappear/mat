package com.example.mat.service;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;
import com.example.mat.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    // 게시물 등록
    @Override
    @Transactional
    public Long createPost(BoardDto boardDto) {
        // DTO를 Entity로 변환
        Board board = dtoToEntity(boardDto);
        // 게시물 저장
        Board savedBoard = boardRepository.save(board);
        // 저장된 게시물의 번호 반환
        return savedBoard.getBno();
    }

    // 게시물 수정
    @Override
    @Transactional
    public void updatePost(Long bno, BoardDto boardDto) {
        // 게시물 조회
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다. bno=" + bno));

        // 게시물 정보 수정
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setBoardCategory(boardDto.getBoardCategory());

        // 수정된 게시물 저장
        boardRepository.save(board);
    }

    // 게시물 삭제
    @Override
    @Transactional
    public void deletePost(Long bno) {
        // 게시물 삭제
        boardRepository.deleteById(bno);
    }

    // 게시물 단일 조회
    @Override
    @Transactional(readOnly = true)
    public BoardDto getPost(Long bno) {
        // 게시물 조회
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다. bno=" + bno));

        // Entity를 DTO로 변환하여 반환
        return entityToDto(board);
    }

    // 게시물 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> getPostList() {
        // 모든 게시물 목록 조회 후 DTO로 변환하여 반환
        return boardRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // DTO를 Entity로 변환
    @Override
    public Board dtoToEntity(BoardDto boardDto) {
        return Board.builder()
                .bno(boardDto.getBno())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .viewCount(boardDto.getViewCount())
                .boardCategory(boardDto.getBoardCategory())
                .build();
    }

    // Entity를 DTO로 변환
    @Override
    public BoardDto entityToDto(Board board) {
        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .boardCategory(board.getBoardCategory())
                .build();
    }
}
