package com.example.mat.controller;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.won.BoardCategoryDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.BoardImage;
import com.example.mat.service.BoardCategoryService;
import com.example.mat.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardCategoryService boardCategoryService;

    // 절대 경로로 업로드 경로 설정
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

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
        List<BoardCategoryDto> categories = boardCategoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("boardDto", new BoardDto());
        return "board/register";
    }

    /**
     * 게시물 등록 처리
     */
    @PostMapping("/register")
    public String register(@ModelAttribute BoardDto boardDto,
            @RequestParam("image") MultipartFile file) {
        try {
            // DTO 및 파일 확인 로그
            System.out.println("Board DTO: " + boardDto);
            System.out.println("File: " + (file != null ? file.getOriginalFilename() : "No file uploaded"));

            // 파일 저장
            BoardImage image = null;
            if (file != null && !file.isEmpty()) {
                image = saveImage(file);
            }

            // 서비스 호출
            boardService.registerWithImage(boardDto, image);
        } catch (Exception e) {
            e.printStackTrace(); // 모든 예외 로깅
            return "redirect:/board/register?error";
        }
        return "redirect:/board/list";
    }

    /**
     * 이미지 저장 로직
     *
     * @param file 업로드할 이미지 파일
     * @return 저장된 BoardImage 엔티티
     * @throws IOException 파일 저장 중 예외
     */
    private BoardImage saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 업로드 경로 생성
        File uploadPath = new File(UPLOAD_DIR);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        // 파일 저장
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(uploadPath, fileName);
        file.transferTo(saveFile);

        // 이미지 정보 생성
        return BoardImage.builder()
                .uuid(uuid)
                .imgName(file.getOriginalFilename())
                .path(UPLOAD_DIR)
                .build();
    }
}
