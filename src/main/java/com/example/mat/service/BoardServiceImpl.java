package com.example.mat.service;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardImage;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.BoardCategoryRepository;
import com.example.mat.repository.BoardRepository;
import com.example.mat.repository.BoardImageRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.util.HtmlUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        private final MemberRepository memberRepository;
        private final BoardImageRepository boardImageRepository;

        @Value("${com.example.mat.upload.path}")
        private String uploadPath;

        @Override
        @Transactional
        public Long register(BoardDto boardDto, MultipartFile file) {
                Member member = memberRepository.findById(boardDto.getMemberId())
                                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

                Board board = Board.builder()
                                .title(boardDto.getTitle())
                                .content(boardDto.getContent())
                                .viewCount(0L)
                                .member(member)
                                .boardCategory(boardCategoryRepository.findById(boardDto.getCategoryId())
                                                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID입니다.")))
                                .build();

                if (file != null && !file.isEmpty()) {
                        handleImageProcessing(board, file);
                }

                return boardRepository.save(board).getBno();
        }

        @Override
        @Transactional
        public Long modify(BoardDto boardDto, MultipartFile file) {
                if (boardDto.getMemberId() == null) {
                        throw new IllegalArgumentException("회원 정보가 누락되었습니다.");
                }

                Board board = boardRepository.findById(boardDto.getBno())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "수정하려는 게시물을 찾을 수 없습니다. ID: " + boardDto.getBno()));

                Member member = memberRepository.findById(boardDto.getMemberId())
                                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

                board.setTitle(boardDto.getTitle());
                board.setContent(boardDto.getContent());
                board.setMember(member);
                board.setBoardCategory(boardCategoryRepository.findById(boardDto.getCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID입니다.")));

                // 이미지 파일이 있으면 처리
                if (file != null && !file.isEmpty()) {
                        handleImageProcessing(board, file);
                }

                return board.getBno();
        }

        @Override
        @Transactional
        public void delete(Long bno) {
                Board board = boardRepository.findById(bno)
                                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 게시물을 찾을 수 없습니다. ID: " + bno));

                if (board.getImage() != null) {
                        BoardImage existingImage = board.getImage();
                        try {
                                Files.deleteIfExists(Paths.get(uploadPath, existingImage.getImgName()));
                        } catch (IOException e) {
                                throw new RuntimeException("이미지를 삭제하지 못했습니다.", e);
                        }
                        boardImageRepository.delete(existingImage);
                }

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
                                .content(HtmlUtil.convertNewlinesToHtml(board.getContent()))
                                .memberId(board.getMember().getMid())
                                .userid(board.getMember().getUserid())
                                .viewCount(board.getViewCount() != null ? board.getViewCount() : 0L)
                                .regDate(board.getRegDate())
                                .updateDate(board.getUpdateDate())
                                .categoryId(board.getBoardCategory() != null ? board.getBoardCategory().getBoardCNo()
                                                : null)
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
                                                .memberId(board.getMember().getMid())
                                                .userid(board.getMember().getUserid())
                                                .viewCount(board.getViewCount() != null ? board.getViewCount() : 0L)
                                                .regDate(board.getRegDate())
                                                .updateDate(board.getUpdateDate())
                                                .categoryId(board.getBoardCategory() != null
                                                                ? board.getBoardCategory().getBoardCNo()
                                                                : null)
                                                .imageFileName(board.getImage() != null ? board.getImage().getImgName()
                                                                : null)
                                                .build());
        }

        @Override
        @Transactional(readOnly = true)
        public Long getMemberIdByUserId(String userId) {
                Member member = memberRepository.findByUserid(userId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 ID를 찾을 수 없습니다: " + userId));
                return member.getMid();
        }

        private void handleImageProcessing(Board board, MultipartFile imageFile) {
                if (board.getImage() != null) {
                        BoardImage existingImage = board.getImage();
                        try {
                                // 기존 파일 삭제
                                Path existingFilePath = Paths.get(uploadPath, existingImage.getImgName());
                                Files.deleteIfExists(existingFilePath);

                                // 기존 이미지 데이터베이스에서 삭제
                                boardImageRepository.delete(existingImage);
                        } catch (IOException e) {
                                throw new RuntimeException("기존 이미지를 삭제하지 못했습니다: " + existingImage.getImgName(), e);
                        }
                }

                // 새 이미지 저장
                String savedFilePath = saveFile(imageFile);
                String uniqueUuid = generateUniqueUuid();

                // 새 이미지 엔티티 생성
                BoardImage newImage = BoardImage.builder()
                                .imgName(savedFilePath)
                                .uuid(uniqueUuid)
                                .board(board)
                                .path(uploadPath) // 저장 경로 포함
                                .build();

                // 게시물에 새 이미지 설정
                board.setImage(newImage);
        }

        private String generateUniqueUuid() {
                String uuid;
                do {
                        uuid = UUID.randomUUID().toString();
                } while (boardImageRepository.existsByUuid(uuid));
                return uuid;
        }

        private String saveFile(MultipartFile file) {
                if (file == null || file.isEmpty()) {
                        return null;
                }

                String fileName;
                do {
                        String uuid = UUID.randomUUID().toString();
                        fileName = uuid + "_" + file.getOriginalFilename();
                } while (boardImageRepository.existsByUuid(fileName.split("_")[0])); // UUID 중복 체크

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
