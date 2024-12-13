package com.example.mat.controller;

import com.example.mat.dto.won.BoardDto;
import com.example.mat.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/board") // 모든 경로를 /board로 묶음
public class BoardController {

    private final BoardService boardService;

    // 게시물 작성 페이지 이동
    @GetMapping("/write") // /board/write 경로
    public String showWritePostPage() {
        return "board/boardWrite"; // boardWrite.html 페이지로 이동
    }

    // 게시물 등록 처리
    @PostMapping("/submit") // /board/submit 경로
    public String submitPost(BoardDto boardDto,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Model model) {

        // 게시물 등록
        Long bno = boardService.createPost(boardDto);
        model.addAttribute("bno", bno); // 등록된 게시물 번호 전달
        return "redirect:/board/content/" + bno; // 게시물 상세 페이지로 리다이렉트
    }

    // 게시물 목록 조회
    @GetMapping("/list") // /board/list 경로
    public String listPosts(Model model) {
        // 게시물 목록을 가져와서 boardList.html에 전달
        List<BoardDto> boardList = boardService.getPostList();
        model.addAttribute("boardList", boardList);
        return "board/boardList"; // boardList.html 페이지로 이동
    }

    // 게시물 상세 조회
    @GetMapping("/content/{bno}") // /board/content/{bno} 경로
    public String viewPost(@PathVariable Long bno, Model model) {
        BoardDto boardDto = boardService.getPost(bno);
        model.addAttribute("board", boardDto); // 게시물 정보 전달
        return "board/boardContent"; // boardContent.html 페이지로 이동
    }

    // 게시물 수정 페이지 이동
    @GetMapping("/edit/{bno}") // /board/edit/{bno} 경로
    public String showEditPostPage(@PathVariable Long bno, Model model) {
        BoardDto boardDto = boardService.getPost(bno);
        model.addAttribute("board", boardDto); // 게시물 정보 전달
        return "board/boardEdit"; // boardEdit.html 페이지로 이동
    }

    // 게시물 수정 처리
    @PostMapping("/update/{bno}") // /board/update/{bno} 경로
    public String updatePost(@PathVariable Long bno, BoardDto boardDto) {
        boardService.updatePost(bno, boardDto); // 게시물 수정
        return "redirect:/board/content/" + bno; // 수정된 게시물 상세 페이지로 리다이렉트
    }

    // 게시물 삭제 처리
    @GetMapping("/delete/{bno}") // /board/delete/{bno} 경로
    public String deletePost(@PathVariable Long bno) {
        boardService.deletePost(bno); // 게시물 삭제
        return "redirect:/board/"; // 게시물 목록 페이지로 리다이렉트
    }

    @GetMapping("/exContent") // boardContent 임시보여주기
    public String testshowContent() {
        return "board/boardContent";
    }

}
