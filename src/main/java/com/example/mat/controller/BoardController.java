package com.example.mat.controller;

import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.service.BoardCategoryService;
import com.example.mat.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
@Log4j2
public class BoardController {

    private final BoardService boardService;
    private final BoardCategoryService boardCategoryService;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            Pageable pageable, Model model) {
        log.info("[REQUEST] board list page");
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();
        var boards = boardService.getList(keyword, category, pageable);

        model.addAttribute("categories", categories);
        model.addAttribute("boards", boards);

        return "board/list";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        log.info("[REQUEST] board register form");
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("boardDto", new BoardDto());
        return "board/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute BoardDto boardDto, @RequestParam("imageFile") MultipartFile file) {
        log.info("[REQUEST] board register process");

        try {
            setMemberIdFromAuth(boardDto);
            boardService.register(boardDto, file);
            log.info("[SUCCESS] 게시물 등록 성공");
            return "redirect:/board/list";
        } catch (Exception e) {
            log.error("[ERROR] 게시물 등록 중 오류 발생", e);
            return "redirect:/board/register?error=true&message=" + e.getMessage();
        }
    }

    @GetMapping("/detail/{bno}")
    public String detail(@PathVariable Long bno, Model model) {
        log.info("[REQUEST] board detail page, bno: {}", bno);

        try {
            BoardDto boardDto = boardService.getDetail(bno);
            model.addAttribute("board", boardDto);
            return "board/detail";
        } catch (Exception e) {
            log.error("[ERROR] 게시물 상세보기 중 오류 발생", e);
            return "redirect:/board/list?error=true";
        }
    }

    @GetMapping("/modify/{bno}")
    public String modifyForm(@PathVariable Long bno, Model model) {
        log.info("[REQUEST] board modify form, bno: {}", bno);

        try {
            BoardDto boardDto = boardService.getDetail(bno);
            List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();

            model.addAttribute("boardDto", boardDto);
            model.addAttribute("categories", categories);
            return "board/modify";
        } catch (Exception e) {
            log.error("[ERROR] 게시물 수정 페이지 로드 중 오류 발생", e);
            return "redirect:/board/list?error=true";
        }
    }

    @PostMapping("/modify")
    public String modify(@ModelAttribute BoardDto boardDto, @RequestParam("imageFile") MultipartFile file) {
        log.info("[REQUEST] board modify process");
        log.info("[DATA] BoardDto: {}, file: {}", boardDto, (file != null ? file.getOriginalFilename() : "No file"));

        try {
            setMemberIdFromAuth(boardDto);
            boardService.modify(boardDto, file);
            log.info("[SUCCESS] 게시물 수정 성공. 게시물 번호: {}", boardDto.getBno());
            return "redirect:/board/detail/" + boardDto.getBno();
        } catch (Exception e) {
            log.error("[ERROR] 게시물 수정 중 예외 발생: {}", e.getMessage(), e);
            return "redirect:/board/modify/" + boardDto.getBno() + "?error=true&message=" + e.getMessage();
        }
    }

    @PostMapping("/delete/{bno}")
    public String delete(@PathVariable Long bno) {
        log.info("[REQUEST] board delete process, bno: {}", bno);

        try {
            boardService.delete(bno);
            log.info("[SUCCESS] 게시물 삭제 성공. 게시물 번호: {}", bno);
            return "redirect:/board/list";
        } catch (Exception e) {
            log.error("[ERROR] 게시물 삭제 중 오류 발생", e);
            return "redirect:/board/list?error=true";
        }
    }

    private void setMemberIdFromAuth(BoardDto boardDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        boardDto.setMemberId(boardService.getMemberIdByUserId(currentUserId));
    }
}
