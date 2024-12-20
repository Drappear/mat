package com.example.mat.controller;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 게시판 컨트롤러
 * 클라이언트의 요청을 처리하고 서비스 계층과 상호작용합니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시물 목록 페이지
     *
     * @param pageRequestDto 페이징 및 검색 요청 정보
     * @param model          뷰에 데이터를 전달하기 위한 모델 객체
     * @return 게시물 목록 페이지 템플릿 이름
     */
    @GetMapping("/list")
    public String list(PageRequestDto pageRequestDto, Model model) {
        PageResultDto<BoardDto, Object[]> result = boardService.getList(pageRequestDto);
        model.addAttribute("result", result);
        return "board/list";
    }

    /**
     * 게시물 등록 페이지로 이동
     *
     * @return 게시물 등록 페이지 템플릿 이름
     */
    @GetMapping("/register")
    public String registerForm() {
        return "board/register";
    }

    /**
     * 게시물 등록 처리
     *
     * @param boardDto 등록할 게시물 정보
     * @return 등록 완료 후 게시물 목록 페이지로 리다이렉트
     */
    @PostMapping("/register")
    public String register(@ModelAttribute BoardDto boardDto) {
        boardService.register(boardDto);
        return "redirect:/board/list";
    }

    /**
     * 게시물 상세 조회 페이지
     *
     * @param bno   조회할 게시물 ID
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 게시물 상세 페이지 템플릿 이름
     */
    @GetMapping("/{bno}")
    public String detail(@PathVariable Long bno, Model model) {
        BoardDto boardDto = boardService.getDetail(bno);
        model.addAttribute("board", boardDto);
        return "board/detail";
    }

    /**
     * 게시물 수정 페이지로 이동
     *
     * @param bno   수정할 게시물 ID
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 게시물 수정 페이지 템플릿 이름
     */
    @GetMapping("/modify/{bno}")
    public String modifyForm(@PathVariable Long bno, Model model) {
        BoardDto boardDto = boardService.getDetail(bno);
        model.addAttribute("board", boardDto);
        return "board/modify";
    }

    /**
     * 게시물 수정 처리
     *
     * @param boardDto 수정할 게시물 정보
     * @return 수정 완료 후 게시물 상세 페이지로 리다이렉트
     */
    @PostMapping("/modify")
    public String modify(@ModelAttribute BoardDto boardDto) {
        boardService.modify(boardDto);
        return "redirect:/board/" + boardDto.getBno();
    }

    /**
     * 게시물 삭제 처리
     *
     * @param bno 삭제할 게시물 ID
     * @return 삭제 완료 후 게시물 목록 페이지로 리다이렉트
     */
    @PostMapping("/delete/{bno}")
    public String delete(@PathVariable Long bno) {
        boardService.delete(bno);
        return "redirect:/board/list";
    }
}
