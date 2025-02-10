package com.example.mat.controller;

import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.dto.won.BoardCommentDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.service.BoardCategoryService;
import com.example.mat.service.BoardCommentService;
import com.example.mat.service.BoardService;
import com.example.mat.util.HtmlUtil;

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
    private final BoardCommentService boardCommentService;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String userid,
            Pageable pageable, Model model) {
        log.info("[REQUEST] board list page");
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();

        var boards = (userid != null && !userid.isEmpty())
                ? boardService.getListByUserid(userid, pageable)
                : boardService.getList(keyword, category, pageable);

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
            // 현재 로그인된 사용자 ID 설정
            setMemberIdFromAuth(boardDto);

            // 게시물 등록 후 생성된 게시물 ID 반환
            Long bno = boardService.register(boardDto, file);
            log.info("[SUCCESS] 게시물 등록 성공, bno: {}", bno);

            // 작성된 게시물의 상세 페이지로 이동
            return "redirect:/board/detail/" + bno;
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
            List<BoardCommentDto> comments = boardCommentService.getCommentsByBoard(bno);
            log.debug("[COMMENTS]: {}", comments); // 댓글 확인
            model.addAttribute("board", boardDto);
            model.addAttribute("comments", comments); // 댓글 추가
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
            boardDto.setContent(HtmlUtil.convertHtmlToText(boardDto.getContent())); // <br> -> 줄바꿈 복원
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
    public String modify(@ModelAttribute BoardDto boardDto,
            @RequestParam("imageFile") MultipartFile file,
            @RequestParam(value = "deleteImage", required = false) String deleteImage) {
        log.info("[REQUEST] board modify process");

        boolean deleteFlag = "true".equals(deleteImage);

        // 파일과 체크박스 상태 확인
        if (!file.isEmpty()) {
            log.info("File uploaded: " + file.getOriginalFilename());
            deleteFlag = true; // 파일이 업로드된 경우, 기존 이미지 삭제
        } else {
            log.info("No file uploaded.");
            deleteFlag = false; // 파일이 없으면 삭제하지 않음
        }

        try {
            boardService.modify(boardDto, file, deleteFlag);
            return "redirect:/board/detail/" + boardDto.getBno();
        } catch (Exception e) {
            log.error("Error during board modification", e);
            return "redirect:/board/modify/" + boardDto.getBno() + "?error=true";
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

    @ModelAttribute
    public void addCategories(Model model) {
        model.addAttribute("categories", boardCategoryService.getAllCategories());
    }
}