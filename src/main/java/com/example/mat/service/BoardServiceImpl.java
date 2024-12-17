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

    @Override
    @Transactional
    public Long createPost(BoardDto boardDto) {
        Board board = dtoToEntity(boardDto);
        board.setViewCount(0L);
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getBno();
    }

    @Override
    @Transactional
    public void updatePost(Long bno, BoardDto boardDto) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다. bno=" + bno));

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setBoardCategory(boardDto.getBoardCategory());

        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void deletePost(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDto getPost(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다. bno=" + bno));
        return entityToDto(board);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> getPostList() {
        return boardRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
