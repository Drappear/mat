package com.example.mat.service;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardCategory;
import com.example.mat.entity.won.BoardImage;
import com.example.mat.repository.BoardCategoryRepository;
import com.example.mat.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    // Upload path configuration
    @Value("${com.example.mat.upload.path}")
    private String uploadPath;

    @Override
    @Transactional
    public Long register(BoardDto boardDto) {
        BoardCategory category = boardCategoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .boardCategory(category)
                .build();

        boardRepository.save(board);
        return board.getBno();
    }

    @Override
    @Transactional
    public Long registerWithImage(BoardDto boardDto, MultipartFile file) {
        BoardCategory category = boardCategoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        BoardImage image = null;
        if (file != null && !file.isEmpty()) {
            try {
                image = saveImage(file);
            } catch (RuntimeException e) {
                System.err.println("Image upload failed: " + e.getMessage());
            }
        }

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .boardCategory(category)
                .image(image)
                .build();

        if (image != null) {
            image.setBoard(board);
        }

        boardRepository.save(board);
        return board.getBno();
    }

    private BoardImage saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFileName;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new RuntimeException("Failed to create upload directory: " + uploadPath);
            }

            File savedFile = new File(uploadDir, fileName);
            file.transferTo(savedFile);

            return BoardImage.builder()
                    .uuid(UUID.randomUUID().toString())
                    .imgName(originalFileName)
                    .path(uploadPath)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Long modify(BoardDto boardDto) {
        Board board = boardRepository.findById(boardDto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        BoardCategory category = boardCategoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setBoardCategory(category);

        return board.getBno();
    }

    @Override
    @Transactional
    public Long modifyWithImage(BoardDto boardDto, MultipartFile file) {
        Board board = boardRepository.findById(boardDto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        BoardCategory category = boardCategoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setBoardCategory(category);

        if (file != null && !file.isEmpty()) {
            BoardImage newImage = saveImage(file);

            if (board.getImage() != null) {
                File existingFile = new File(board.getImage().getPath(), board.getImage().getImgName());
                if (existingFile.exists()) {
                    existingFile.delete();
                }
            }

            newImage.setBoard(board);
            board.setImage(newImage);
        }

        return board.getBno();
    }

    @Override
    @Transactional
    public void delete(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public BoardDto getDetail(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryId(board.getBoardCategory().getBoardCNo())
                .nickname(board.getNickname())
                .viewCount(board.getViewCount())
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .build();
    }

}