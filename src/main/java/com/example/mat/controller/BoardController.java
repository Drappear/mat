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

        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();
        var boards = boardService.getList(keyword, category, pageable);

        model.addAttribute("categories", categories);
        model.addAttribute("boards", boards);

        return "board/list";
    }

    // 게시물 등록 페이지
    @GetMapping("/register")
    public String registerForm(Model model) {
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("boardDto", new BoardDto());

        return "board/register";
    }

    // 게시물 등록 처리
    @PostMapping("/register")
    public String register(@ModelAttribute BoardDto boardDto, @RequestParam("imageFile") MultipartFile file) {
        try {
            if (boardDto.getNick() == null || boardDto.getNick().isBlank()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                boardDto.setNick(authentication.getName());
            }

            boardDto.setImageFile(file);

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
            List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();

            model.addAttribute("boardDto", boardDto);
            model.addAttribute("categories", categories);
            return "board/modify";
        } catch (Exception e) {
            System.err.println("[ERROR] 게시물 수정 페이지 로드 중 오류 발생: " + e.getMessage());
            return "redirect:/board/list?error=true";
        }
    }

    // 게시물 수정 처리
    @PostMapping("/modify")
    public String modify(@ModelAttribute BoardDto boardDto, @RequestParam("imageFile") MultipartFile file) {
        System.out.println("[DEBUG] modify() called with BoardDto: " + boardDto);
        System.out.println("[DEBUG] Received file: " + (file != null ? file.getOriginalFilename() : "No file"));

        try {
            // 파일이 있는 경우, BoardDto에 설정
            if (file != null && !file.isEmpty()) {
                boardDto.setImageFile(file);
            }

            // 데이터 유효성 검사
            if (boardDto.getTitle() == null || boardDto.getTitle().trim().isEmpty()) {
                System.err.println("[ERROR] 제목이 비어 있습니다.");
                return "redirect:/board/modify/" + boardDto.getBno() + "?error=title";
            }

            if (boardDto.getContent() == null || boardDto.getContent().trim().isEmpty()) {
                System.err.println("[ERROR] 내용이 비어 있습니다.");
                return "redirect:/board/modify/" + boardDto.getBno() + "?error=content";
            }

            // 게시물 수정 서비스 호출
            boardService.modify(boardDto);
            System.out.println("[INFO] 게시물 수정 성공. 게시물 번호: " + boardDto.getBno());
            return "redirect:/board/detail/" + boardDto.getBno();
        } catch (Exception e) {
            // 예외 발생 시 오류 로그 출력
            System.err.println("[ERROR] 게시물 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
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
