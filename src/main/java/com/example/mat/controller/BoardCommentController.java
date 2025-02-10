package com.example.mat.controller;

import com.example.mat.dto.won.BoardCommentDto;
import com.example.mat.service.BoardCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    // 댓글 추가
    @PostMapping("/comment")
    public ResponseEntity<Long> addComment(@RequestBody @Valid BoardCommentDto boardCommentDto) {
        log.info("[REQUEST] 댓글 작성 요청: {}", boardCommentDto);

        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            String currentUserId = authentication.getName(); // 현재 사용자 ID
            boardCommentDto.setUserid(currentUserId); // userid 설정
        } else {
            log.error("[ERROR] 인증되지 않은 사용자 요청");
            return ResponseEntity.status(401).build(); // 인증되지 않은 경우 401 응답
        }

        // 댓글 추가 서비스 호출
        Long commentId = boardCommentService.addComment(boardCommentDto);
        return ResponseEntity.ok(commentId);
    }

    // 댓글 목록 조회
    @GetMapping("/{boardId}/comments")
    public ResponseEntity<List<BoardCommentDto>> getComments(@PathVariable Long boardId) {
        log.info("[REQUEST] 댓글 목록 조회 요청, boardId: {}", boardId);

        // 특정 게시글의 댓글 목록 조회
        List<BoardCommentDto> comments = boardCommentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @RequestBody String content) {
        log.info("[REQUEST] 댓글 수정 요청, commentId: {}, content: {}", commentId, content);

        // 댓글 수정 서비스 호출
        boardCommentService.updateComment(commentId, content);
        return ResponseEntity.noContent().build();
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        log.info("[REQUEST] 댓글 삭제 요청, commentId: {}", commentId);

        // 댓글 삭제 서비스 호출
        boardCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
