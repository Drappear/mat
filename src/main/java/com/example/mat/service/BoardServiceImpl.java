package com.example.mat.service;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardImage;
import com.example.mat.entity.shin.Member;
import com.example.mat.entity.shin.MemberImage;
import com.example.mat.repository.BoardCategoryRepository;
import com.example.mat.repository.BoardRepository;
import com.example.mat.repository.BoardImageRepository;
import com.example.mat.repository.BoardCommentRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.shin.MemberImageRepository;
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
        private final MemberImageRepository memberImageRepository;
        private final BoardImageRepository boardImageRepository;
        private final BoardCommentRepository boardCommentRepository;

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

                // **게시글 먼저 저장**
                board = boardRepository.save(board);

                // **이미지 처리**
                if (file != null && !file.isEmpty()) {
                        handleImageProcessing(board, file);
                }

                return board.getBno();
        }

        @Override
        @Transactional
        public Long modify(BoardDto boardDto, MultipartFile file, boolean deleteImage) {
                Board board = boardRepository.findById(boardDto.getBno())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "수정하려는 게시물을 찾을 수 없습니다. ID: " + boardDto.getBno()));

                if (deleteImage) {
                        deleteExistingImage(board);
                }

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

                deleteExistingImage(board);
                boardRepository.delete(board);
        }

        @Override
        @Transactional
        public BoardDto getDetail(Long bno) {
                Board board = boardRepository.findById(bno)
                                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다. ID: " + bno));

                board.setViewCount(board.getViewCount() + 1);
                boardRepository.save(board);

                return convertToDto(board);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<BoardDto> getList(String keyword, Long category, Pageable pageable) {
                return boardRepository.findByKeywordAndCategory(keyword, category, pageable)
                                .map(this::convertToDto);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<BoardDto> getListByUserid(String userid, Pageable pageable) {
                return boardRepository.findByUserid(userid, pageable)
                                .map(this::convertToDto);
        }

        @Override
        @Transactional(readOnly = true)
        public Long getMemberIdByUserId(String userId) {
                Member member = memberRepository.findByUserid(userId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 ID를 찾을 수 없습니다: " + userId));
                return member.getMid();
        }

        private BoardDto convertToDto(Board board) {
                return BoardDto.builder()
                                .bno(board.getBno())
                                .title(board.getTitle())
                                .content(HtmlUtil.convertNewlinesToHtml(board.getContent()))
                                .memberId(board.getMember().getMid())
                                .userid(board.getMember().getUserid())
                                .viewCount(board.getViewCount())
                                .regDate(board.getRegDate())
                                .updateDate(board.getUpdateDate())
                                .categoryId(board.getBoardCategory() != null ? board.getBoardCategory().getBoardCNo()
                                                : null)
                                .imageFileName(board.getImage() != null ? board.getImage().getImgName() : null)
                                .commentCount(boardCommentRepository.countByBoardId(board.getBno()))
                                .profileImage(getProfileImageByMember(board.getMember()))
                                .build();
        }

        private String getProfileImageByMember(Member member) {
                if (member == null) {
                        return "/images/default-profile.png";
                }

                MemberImage memberImage = memberImageRepository.findByMember(member);
                if (memberImage != null) {
                        return "/member/display?fileName=" + memberImage.getUuid() + "_" + memberImage.getImgName();
                }

                return "/images/default-profile.png";
        }

        private void handleImageProcessing(Board board, MultipartFile imageFile) {
                deleteExistingImage(board);
                String savedFilePath = saveFile(imageFile);
                String uniqueUuid = generateUniqueUuid();

                BoardImage newImage = BoardImage.builder()
                                .imgName(savedFilePath)
                                .uuid(uniqueUuid)
                                .board(board)
                                .path(uploadPath)
                                .build();
                board.setImage(newImage);
                boardImageRepository.save(newImage);
        }

        private void deleteExistingImage(Board board) {
                if (board.getImage() != null) {
                        try {
                                Path imagePath = Paths.get(uploadPath, board.getImage().getImgName());
                                Files.deleteIfExists(imagePath);
                                boardImageRepository.delete(board.getImage());
                                board.setImage(null);
                        } catch (IOException e) {
                                throw new RuntimeException("기존 이미지를 삭제하지 못했습니다.", e);
                        }
                }
        }

        private String saveFile(MultipartFile file) {
                if (file == null || file.isEmpty()) {
                        throw new IllegalArgumentException("업로드된 파일이 없습니다.");
                }

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadPath, fileName);

                try {
                        Files.createDirectories(filePath.getParent());
                        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        return fileName;
                } catch (IOException e) {
                        throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage(), e);
                }
        }

        private String generateUniqueUuid() {
                String uuid;
                do {
                        uuid = UUID.randomUUID().toString();
                } while (boardImageRepository.existsByUuid(uuid));
                return uuid;
        }
}
