package com.example.mat.controller;

import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.service.BoardCategoryService;
import com.example.mat.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardCategoryService boardCategoryService;

    // 게시판 목록 페이지
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            @PageableDefault(size = 10, sort = "regDate") Pageable pageable,
            Model model) {

        // 카테고리 목록 가져오기
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();

        // 게시물 목록 가져오기
        var boards = boardService.getList(keyword, category, pageable);

        // 모델에 데이터 추가
        model.addAttribute("categories", categories);
        model.addAttribute("boards", boards);

        return "board/list";
    }

    // 게시물 등록 페이지
    @GetMapping("/register")
    public String registerForm(Model model) {
        // 카테고리 목록 가져오기
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();

        // 카테고리 데이터 확인용 로그 추가
        if (categories.isEmpty()) {
            System.out.println("[ERROR] 카테고리 데이터를 불러오지 못했습니다!");
        } else {
            System.out.println("[INFO] 카테고리 목록: " + categories);
        }

        // 모델에 데이터 추가
        model.addAttribute("categories", categories);
        model.addAttribute("boardDto", new BoardDto());

        return "board/register";
    }

    // 게시물 등록 처리
    @PostMapping("/register")
    public String register(@ModelAttribute BoardDto boardDto) {
        try {
            boardService.register(boardDto);
            return "redirect:/board/list";
        } catch (Exception e) {
            System.err.println("[ERROR] 게시물 등록 중 오류 발생: " + e.getMessage());
            return "redirect:/board/register?error=true";
        }
    }

    // 게시물 상세보기
    @GetMapping("/detail/{bno}")
    public String detail(@PathVariable Long bno, Model model) {
        try {
            BoardDto boardDto = boardService.getDetail(bno);
            model.addAttribute("board", boardDto);
            return "board/detail";
        } catch (Exception e) {
            System.err.println("[ERROR] 게시물 상세보기 중 오류 발생: " + e.getMessage());
            return "redirect:/board/list?error=true";
        }
    }

    // 게시물 수정 페이지
    @GetMapping("/modify/{bno}")
    public String modifyForm(@PathVariable Long bno, Model model) {
        try {
            BoardDto boardDto = boardService.getDetail(bno);
            model.addAttribute("boardDto", boardDto);
            return "board/modify";
        } catch (Exception e) {
            System.err.println("[ERROR] 게시물 수정 페이지 로드 중 오류 발생: " + e.getMessage());
            return "redirect:/board/list?error=true";
        }
    }

    // 게시물 수정 처리
    @PostMapping("/modify")
    public String modify(@ModelAttribute BoardDto boardDto) {
        try {
            boardService.modify(boardDto);
            return "redirect:/board/detail/" + boardDto.getBno();
        } catch (Exception e) {
            System.err.println("[ERROR] 게시물 수정 중 오류 발생: " + e.getMessage());
            return "redirect:/board/modify/" + boardDto.getBno() + "?error=true";
        }
    }

    // 게시물 삭제 처리
    @PostMapping("/delete/{bno}")
    public String delete(@PathVariable Long bno) {
        try {
            boardService.delete(bno);
            return "redirect:/board/list";
        } catch (Exception e) {
            System.err.println("[ERROR] 게시물 삭제 중 오류 발생: " + e.getMessage());
            return "redirect:/board/list?error=true";
        }
    }
}
