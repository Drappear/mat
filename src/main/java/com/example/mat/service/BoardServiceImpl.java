package com.example.mat.service;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardImage;
import com.example.mat.repository.BoardCategoryRepository;
import com.example.mat.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    @Value("${com.example.mat.upload.path}")
    private String uploadPath;

    @Override
    @Transactional
    public Long register(BoardDto boardDto) {
        if (boardDto.getNick() == null || boardDto.getNick().isBlank()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boardDto.setNick(authentication.getName());
        }

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .nick(boardDto.getNick())
                .viewCount(0L)
                .boardCategory(boardCategoryRepository.findById(boardDto.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID입니다.")))
                .build();

        // 이미지 파일 처리
        if (boardDto.getImageFile() != null && !boardDto.getImageFile().isEmpty()) {
            String savedFilePath = saveFile(boardDto.getImageFile());
            board.setImage(BoardImage.builder()
                    .imgName(savedFilePath)
                    .board(board)
                    .build());
        }

        return boardRepository.save(board).getBno();
    }

    @Override
    @Transactional
    public Long modify(BoardDto boardDto) {
        Board board = boardRepository.findById(boardDto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("수정하려는 게시물을 찾을 수 없습니다. ID: " + boardDto.getBno()));

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setNick(boardDto.getNick());

        // 이미지 파일 처리
        if (boardDto.getImageFile() != null && !boardDto.getImageFile().isEmpty()) {
            String savedFilePath = saveFile(boardDto.getImageFile());
            board.setImage(BoardImage.builder()
                    .imgName(savedFilePath)
                    .board(board)
                    .build());
        } else if (board.getImage() != null) {
            boardDto.setImageFileName(board.getImage().getImgName());
        }

        return board.getBno();
    }

    @Override
    @Transactional
    public void delete(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 게시물을 찾을 수 없습니다. ID: " + bno));
        boardRepository.delete(board);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDto getDetail(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다. ID: " + bno));

        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .nick(board.getNick() != null ? board.getNick() : "Anonymous")
                .viewCount(board.getViewCount() != null ? board.getViewCount() : 0L)
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .categoryId(board.getBoardCategory() != null ? board.getBoardCategory().getBoardCNo() : null)
                .imageFileName(board.getImage() != null ? board.getImage().getImgName() : null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoardDto> getList(String keyword, Long category, Pageable pageable) {
        return boardRepository.findByKeywordAndCategory(keyword, category, pageable)
                .map(board -> BoardDto.builder()
                        .bno(board.getBno())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .nick(board.getNick() != null ? board.getNick() : "Anonymous")
                        .viewCount(board.getViewCount() != null ? board.getViewCount() : 0L)
                        .regDate(board.getRegDate())
                        .updateDate(board.getUpdateDate())
                        .categoryId(board.getBoardCategory() != null ? board.getBoardCategory().getBoardCNo() : null)
                        .imageFileName(board.getImage() != null ? board.getImage().getImgName() : null)
                        .build());
    }

    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
        }
    }
}
