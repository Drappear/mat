package com.example.mat.service;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;
import com.example.mat.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Value("${com.example.mat.upload.path}")
    private String uploadPath;

    @Override
    @Transactional
    public Long register(BoardDto boardDto) {
        // Logic for registering a new board
        return null;
    }

    @Override
    @Transactional
    public Long registerWithImage(BoardDto boardDto, MultipartFile file) {
        // Logic for registering a new board with an image
        return null;
    }

    @Override
    @Transactional
    public Long modify(BoardDto boardDto) {
        // Logic for modifying a board
        return null;
    }

    @Override
    @Transactional
    public Long modifyWithImage(BoardDto boardDto, MultipartFile file) {
        // Logic for modifying a board with an image
        return null;
    }

    @Override
    @Transactional
    public void delete(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDto getDetail(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .nick(board.getNick() != null ? board.getNick() : "Anonymous") // Null check
                .viewCount(board.getViewCount() != null ? board.getViewCount() : 0L) // Null check
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .categoryId(board.getBoardCategory() != null ? board.getBoardCategory().getBoardCNo() : null) // Null
                                                                                                              // check
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> getList() {
        return boardRepository.findAll().stream()
                .map(board -> BoardDto.builder()
                        .bno(board.getBno())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .nick(board.getNick() != null ? board.getNick() : "Anonymous") // Null check
                        .viewCount(board.getViewCount() != null ? board.getViewCount() : 0L) // Null check
                        .regDate(board.getRegDate())
                        .updateDate(board.getUpdateDate())
                        .categoryId(board.getBoardCategory() != null ? board.getBoardCategory().getBoardCNo() : null) // Null
                                                                                                                      // check
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoardDto> getList(String keyword, Long category, Pageable pageable) {
        return boardRepository.findByKeywordAndCategory(keyword, category, pageable)
                .map(board -> BoardDto.builder()
                        .bno(board.getBno())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .nick(board.getNick() != null ? board.getNick() : "Anonymous") // Null check
                        .viewCount(board.getViewCount() != null ? board.getViewCount() : 0L) // Null check
                        .regDate(board.getRegDate())
                        .updateDate(board.getUpdateDate())
                        .categoryId(board.getBoardCategory() != null ? board.getBoardCategory().getBoardCNo() : null) // Null
                                                                                                                      // check
                        .build());
    }
}
