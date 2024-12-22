package com.example.mat.controller;

import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.service.BoardCategoryService;
import com.example.mat.service.BoardService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            Pageable pageable,
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

        // 모델에 데이터 추가
        model.addAttribute("categories", categories);
        model.addAttribute("boardDto", new BoardDto());

        return "board/register";
    }

    // 게시물 등록 처리
    @PostMapping("/register")
    public String register(@ModelAttribute BoardDto boardDto, @RequestParam("imageFile") MultipartFile file) {
        try {
            // 닉네임 설정
            if (boardDto.getNick() == null || boardDto.getNick().isBlank()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                boardDto.setNick(authentication.getName());
            }

            // 이미지 파일 처리
            if (file != null && !file.isEmpty()) {
                boardDto.setImageFileName(file);
            }

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
    public String modify(@ModelAttribute BoardDto boardDto, @RequestParam("imageFile") MultipartFile file) {
        try {
            // 이미지 파일 처리
            if (file != null && !file.isEmpty()) {
                boardDto.setImageFileName(file);
            }

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
