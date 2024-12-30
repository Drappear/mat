package com.example.mat.service;

import com.example.mat.dto.won.BoardCommentDto;
import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardComment;
import com.example.mat.repository.BoardCommentRepository;
import com.example.mat.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService {

        private final BoardCommentRepository boardCommentRepository;
        private final BoardRepository boardRepository;

        @Override
        @Transactional
        public Long addComment(BoardCommentDto boardCommentDto) {
                log.info("[Service] 댓글 추가 요청: {}", boardCommentDto);

                // 게시글 확인
                Board board = boardRepository.findById(boardCommentDto.getBoardId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "게시글을 찾을 수 없습니다. ID: " + boardCommentDto.getBoardId()));

                // 부모 댓글 확인 (대댓글인 경우)
                BoardComment parentComment = null;
                if (boardCommentDto.getParentId() != null) {
                        parentComment = boardCommentRepository.findById(boardCommentDto.getParentId())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "부모 댓글을 찾을 수 없습니다. ID: " + boardCommentDto.getParentId()));
                }

                // 댓글 엔티티 생성
                BoardComment comment = BoardComment.builder()
                                .content(boardCommentDto.getContent())
                                .userid(boardCommentDto.getUserid())
                                .board(board)
                                .parent(parentComment) // 부모 댓글 설정
                                .build();

                // 저장 및 ID 반환
                BoardComment savedComment = boardCommentRepository.save(comment);
                log.info("[Service] 댓글 추가 완료. 저장된 댓글 ID: {}", savedComment.getBcid());
                return savedComment.getBcid();
        }

        @Override
        @Transactional
        public void updateComment(Long id, String content) {
                BoardComment comment = boardCommentRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("수정하려는 댓글을 찾을 수 없습니다. ID: " + id));
                comment.setContent(content);
                log.info("[Service] 댓글 수정 완료. ID: {}, 내용: {}", id, content);
        }

        @Override
        @Transactional
        public void deleteComment(Long id) {
                BoardComment comment = boardCommentRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 댓글을 찾을 수 없습니다. ID: " + id));
                boardCommentRepository.delete(comment);
                log.info("[Service] 댓글 삭제 완료. ID: {}", id);
        }

        @Override
        @Transactional(readOnly = true)
        public List<BoardCommentDto> getCommentsByBoard(Long boardId) {
                log.info("[Service] 댓글 목록 조회 요청. 게시글 ID: {}", boardId);

                Board board = boardRepository.findById(boardId)
                                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));

                List<BoardComment> comments = boardCommentRepository
                                .findByBoardAndParentIsNullOrderByRegDateDesc(board);
                log.info("[Service] 부모 댓글 목록 조회 완료. 총 {}개", comments.size());

                // 부모 댓글만 필터링하고 대댓글 포함 DTO로 변환
                return comments.stream()
                                .map(this::convertToDtoWithReplies) // 대댓글 포함 DTO로 변환
                                .collect(Collectors.toList());
        }

        private BoardCommentDto convertToDtoWithReplies(BoardComment comment) {
                return BoardCommentDto.builder()
                                .bcid(comment.getBcid()) // 변경된 부분
                                .content(comment.getContent())
                                .userid(comment.getUserid())
                                .boardId(comment.getBoard().getBno())
                                .parentId(comment.getParent() != null ? comment.getParent().getBcid() : null) // 변경된 부분
                                .regDate(comment.getRegDate())
                                .updateDate(comment.getUpdateDate())
                                .replies(comment.getReplies() != null
                                                ? comment.getReplies().stream().map(this::convertToDtoWithReplies)
                                                                .collect(Collectors.toList())
                                                : null)
                                .build();
        }
}
