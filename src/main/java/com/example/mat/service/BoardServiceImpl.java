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
import lombok.extern.log4j.Log4j2;

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

@Log4j2
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
        public Long modify(BoardDto boardDto, MultipartFile file, boolean deleteImage) {
                Board board = boardRepository.findById(boardDto.getBno())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "수정하려는 게시물을 찾을 수 없습니다. ID: " + boardDto.getBno()));

                // 기존 이미지 삭제
                if (deleteImage && board.getImage() != null) {
                        BoardImage existingImage = board.getImage();
                        try {
                                Path existingFilePath = Paths.get(uploadPath, existingImage.getImgName());
                                Files.deleteIfExists(existingFilePath);
                        } catch (IOException e) {
                                throw new RuntimeException("기존 이미지를 삭제하지 못했습니다.", e);
                        }
                        boardImageRepository.delete(existingImage);
                        board.setImage(null); // 이미지 연결 해제
                }

                // 새 이미지 처리
                if (file != null && !file.isEmpty()) {
                        handleImageProcessing(board, file);
                }

                board.setTitle(boardDto.getTitle());
                board.setContent(boardDto.getContent());
                board.setBoardCategory(boardCategoryRepository.findById(boardDto.getCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID입니다.")));

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
        public Page<BoardDto> getListByUserid(String userid, Pageable pageable) {
                return boardRepository.findByUserid(userid, pageable)
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
                // 기존 이미지 삭제
                if (board.getImage() != null) {
                        BoardImage existingImage = board.getImage();
                        try {
                                Path existingFilePath = Paths.get(uploadPath, existingImage.getImgName());
                                boolean deleted = Files.deleteIfExists(existingFilePath); // 파일 삭제
                                if (deleted) {
                                        log.info("기존 이미지 파일 삭제 성공: " + existingFilePath);
                                } else {
                                        log.warn("기존 이미지 파일을 찾을 수 없습니다: " + existingFilePath);
                                }
                        } catch (IOException e) {
                                log.error("기존 이미지를 삭제하는 중 에러 발생: " + existingImage.getImgName(), e);
                                throw new RuntimeException("기존 이미지를 삭제하지 못했습니다.", e);
                        }
                        boardImageRepository.delete(existingImage); // 데이터베이스에서 이미지 삭제
                        log.info("기존 이미지 데이터베이스 삭제 성공");
                }

                // 새 이미지 저장
                String savedFilePath = saveFile(imageFile);
                String uniqueUuid = generateUniqueUuid();
                BoardImage newImage = BoardImage.builder()
                                .imgName(savedFilePath)
                                .uuid(uniqueUuid)
                                .board(board)
                                .path(uploadPath)
                                .build();
                board.setImage(newImage); // 새 이미지 설정
                log.info("새 이미지 저장 성공: " + savedFilePath);
        }

        private String generateUniqueUuid() {
                String uuid;
                do {
                        uuid = UUID.randomUUID().toString();
                } while (boardImageRepository.existsByUuid(uuid));
                log.info("생성된 UUID: " + uuid);
                return uuid;
        }

        private String saveFile(MultipartFile file) {
                if (file == null || file.isEmpty()) {
                        log.warn("파일이 비어 있습니다.");
                        return null;
                }

                String fileName;
                do {
                        String uuid = UUID.randomUUID().toString();
                        fileName = uuid + "_" + file.getOriginalFilename();
                } while (boardImageRepository.existsByUuid(fileName.split("_")[0]));

                Path filePath = Paths.get(uploadPath, fileName);

                try {
                        Files.createDirectories(filePath.getParent()); // 경로가 없으면 생성
                        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        log.info("파일 저장 성공: " + filePath);
                        return fileName;
                } catch (IOException e) {
                        log.error("파일 저장 실패: " + filePath, e);
                        throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
                }
        }

}
