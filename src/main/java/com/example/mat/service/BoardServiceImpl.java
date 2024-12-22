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
        // 닉네임이 없으면 로그인한 사용자의 닉네임으로 설정
        if (boardDto.getNick() == null || boardDto.getNick().isBlank()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // AuthMemberDto를 사용해 로그인된 사용자의 닉네임 가져오기
            boardDto.setNick(authentication.getName());
        }

        // 게시물 엔티티 생성
        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .nick(boardDto.getNick())
                .viewCount(0L) // 명시적으로 viewCount 초기화
                .boardCategory(boardCategoryRepository.findById(boardDto.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID입니다.")))
                .build();

        // 이미지 처리
        if (boardDto.getImageFileName() != null && !boardDto.getImageFileName().isEmpty()) {
            String savedFilePath = saveFile(boardDto.getImageFileName());
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
        // 수정할 게시물 가져오기
        Board board = boardRepository.findById(boardDto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("수정하려는 게시물을 찾을 수 없습니다. ID: " + boardDto.getBno()));

        // 수정 내용 반영
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setNick(boardDto.getNick());

        // 이미지가 존재하는 경우 업데이트
        if (boardDto.getImageFileName() != null && !boardDto.getImageFileName().isEmpty()) {
            String savedFilePath = saveFile(boardDto.getImageFileName());
            board.setImage(BoardImage.builder()
                    .imgName(savedFilePath)
                    .board(board)
                    .build());
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
                        .build());
    }

    // 파일 저장 로직
    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName); // 업로드 경로와 파일 이름 조합

        try {
            Files.createDirectories(filePath.getParent()); // 경로가 없으면 생성
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING); // 파일 저장
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e); // 저장 실패 시 예외 발생
        }
    }

}
