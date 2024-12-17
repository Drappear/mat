package com.example.mat.controller;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시물 작성 페이지 이동
    @GetMapping("/write")
    public String showWritePostPage() {
        log.info("게시물 작성 페이지로 이동");
        return "board/boardWrite";
    }

    // 게시물 등록 처리
    @PostMapping("/submit")
    public String submitPost(@Valid BoardDto boardDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.warn("유효성 검증 실패: {}", bindingResult.getAllErrors());
            model.addAttribute("errorMessages", bindingResult.getAllErrors());
            return "board/boardWrite";
        }

        Long bno = boardService.createPost(boardDto);
        log.info("게시물 등록 완료 (bno: {})", bno);
        return "redirect:/board/content/" + bno;
    }

    // 게시물 목록 조회
    @GetMapping("/list")
    public String listPosts(Model model) {
        List<BoardDto> boardList = boardService.getPostList();
        model.addAttribute("boardList", boardList);
        log.info("게시물 목록 조회 (총 {}건)", boardList.size());
        return "board/boardList";
    }

    // 게시물 상세 조회
    @GetMapping("/content/{bno}")
    public String viewPost(@PathVariable Long bno, Model model) {
        BoardDto boardDto = boardService.getPost(bno);

        // 게시물이 null인 경우
        if (boardDto == null) {
            log.warn("게시물 상세 조회 실패 (bno: {})", bno);
            return "redirect:/board/list";
        }

        // 게시물 데이터가 제대로 조회된 경우
        log.info("게시물 조회 성공: {}", boardDto);

        // 게시물 정보를 모델에 추가
        model.addAttribute("board", boardDto);

        log.info("게시물 상세 조회 (bno: {})", bno);
        return "board/boardContent";
    }

    // 게시물 수정 페이지 이동
    @GetMapping("/edit/{bno}")
    public String showEditPostPage(@PathVariable Long bno, Model model) {
        BoardDto boardDto = boardService.getPost(bno);
        if (boardDto == null) {
            log.warn("게시물 수정 페이지 이동 실패 (bno: {})", bno);
            return "redirect:/board/list";
        }
        model.addAttribute("board", boardDto);
        log.info("게시물 수정 페이지로 이동 (bno: {})", bno);
        return "board/boardEdit";
    }

    // 게시물 수정 처리
    @PostMapping("/update/{bno}")
    public String updatePost(@PathVariable Long bno, @Valid BoardDto boardDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("게시물 수정 유효성 검증 실패 (bno: {})", bno);
            return "board/boardEdit";
        }
        boardService.updatePost(bno, boardDto);
        log.info("게시물 수정 완료 (bno: {})", bno);
        return "redirect:/board/content/" + bno;
    }

    // 게시물 삭제 처리
    @GetMapping("/delete/{bno}")
    public String deletePost(@PathVariable Long bno) {
        boardService.deletePost(bno);
        log.info("게시물 삭제 완료 (bno: {})", bno);
        return "redirect:/board/list";
    }

    // 임시 게시물 상세 보기 (디자인 확인용)
    @GetMapping("/exContent")
    public String testShowContent() {
        log.info("임시 게시물 상세 보기 페이지 이동");
        return "board/exBoardContent";
    }
}
