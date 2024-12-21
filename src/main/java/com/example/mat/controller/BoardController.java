package com.example.mat.controller;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.service.BoardCategoryService;
import com.example.mat.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시판 컨트롤러
 * 클라이언트의 요청을 처리하고 서비스 계층과 상호작용합니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardCategoryService boardCategoryService;

    /**
     * 게시물 목록 페이지
     */
    @GetMapping("/list")
    public String list(PageRequestDto pageRequestDto, Model model) {
        PageResultDto<BoardDto, Object[]> result = boardService.getList(pageRequestDto);
        model.addAttribute("result", result);
        return "board/list";
    }

    /**
     * 게시물 등록 페이지로 이동
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        // 카테고리 목록 추가
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("boardDto", new BoardDto());
        return "board/register";
    }

    /**
     * 게시물 등록 처리
     */
    @PostMapping("/register")
    public String register(@ModelAttribute BoardDto boardDto, @RequestParam("image") MultipartFile file) {
        try {
            // DTO 및 파일 확인 로그
            System.out.println("Board DTO: " + boardDto);
            System.out.println("File: " + (file != null ? file.getOriginalFilename() : "No file uploaded"));

            // 카테고리 유효성 검사
            if (boardDto.getCategoryId() == null) {
                throw new IllegalArgumentException("카테고리는 필수 항목입니다.");
            }

            // 서비스 호출
            boardService.registerWithImage(boardDto, file);
        } catch (Exception e) {
            e.printStackTrace(); // 모든 예외 로깅
            return "redirect:/board/register?error";
        }
        return "redirect:/board/list";
    }

    /**
     * 게시물 상세 보기
     */
    @GetMapping("/detail/{bno}")
    public String detail(@PathVariable Long bno, Model model) {
        try {
            BoardDto boardDto = boardService.getDetail(bno);
            model.addAttribute("board", boardDto);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/board/list?error";
        }
        return "board/detail";
    }

    /**
     * 게시물 수정 페이지로 이동
     */
    @GetMapping("/modify/{bno}")
    public String modifyForm(@PathVariable Long bno, Model model) {
        try {
            BoardDto boardDto = boardService.getDetail(bno);
            List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("boardDto", boardDto);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/board/list?error";
        }
        return "board/modify";
    }

    /**
     * 게시물 수정 처리
     */
    @PostMapping("/modify")
    public String modify(@ModelAttribute BoardDto boardDto,
            @RequestParam(value = "image", required = false) MultipartFile file) {
        try {
            boardService.modifyWithImage(boardDto, file);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/board/modify/" + boardDto.getBno() + "?error";
        }
        return "redirect:/board/list";
    }

    /**
     * 게시물 삭제 처리
     */
    @PostMapping("/delete/{bno}")
    public String delete(@PathVariable Long bno) {
        try {
            boardService.delete(bno);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/board/list?error";
        }
        return "redirect:/board/list";
    }
}