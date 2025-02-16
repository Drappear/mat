package com.example.mat.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerCategoryDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.service.DinerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/diner")
@Controller
public class DinerController {

    @Value("${com.example.mat.upload.path}")
    private String uploadPath;

    private final DinerService dinerService;

    @GetMapping("/list")
    public void getDinerList(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, Model model) {
        log.info("get diner list 페이지 요청");
        pageRequestDto.setSize(8);
        PageResultDto<DinerDto, Object[]> result = dinerService.getDinerList(pageRequestDto);
        List<DinerCategoryDto> categories = dinerService.getCategoryList();

        model.addAttribute("categories", categories);
        model.addAttribute("result", result);
    }

    @GetMapping("/idx")
    public void getDinerIdx() {
        log.info("get diner idx 페이지 요청");
    }

    @GetMapping("/read")
    public void getDinerRead(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, @RequestParam Long did,
            Model model) {
        log.info("get diner read 페이지 요청");
        DinerDto dinerDto = dinerService.getDinerDetail(did);
        model.addAttribute("dinerDto", dinerDto);
    }

    @GetMapping("/register")
    public void getDinerRegister(DinerDto dinerDto, Model model) {
        log.info("get diner register 페이지 요청");

        List<DinerCategoryDto> categories = dinerService.getCategoryList();

        model.addAttribute("categories", categories);
    }

    @PostMapping("/create")
    public String postCreateDiner(DinerDto dinerDto,
            @ModelAttribute("requestDto") PageRequestDto pageRequestDto,
            RedirectAttributes rttr, @RequestParam(value = "uploadFiles") MultipartFile[] uploadFiles) {
        log.info("식당 등록 {}", dinerDto);

        Long did = dinerService.createDiner(dinerDto);
        
        String uploadId = did.toString();

        for (MultipartFile multipartFile : uploadFiles) {            

            // 사용자가 올린 파일명
            String originName = multipartFile.getOriginalFilename();
            log.info("파일명 :  {}", originName);

            // 저장 폴더 생성
            String saveFolderPath = makeFolder("diner", uploadId);

            // 파일 저장 - uuid(중복 해결)
            String uuid = UUID.randomUUID().toString();

            String saveName = uploadPath + File.separator + saveFolderPath + File.separator + uuid + "_" + originName;

            Path savePath = Paths.get(saveName);

            try {
                // 폴더 저장
                multipartFile.transferTo(savePath);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        log.info("식당 등록 완료 {}", did);

        rttr.addAttribute("did", did);
        rttr.addAttribute("page", 1);
        rttr.addAttribute("size", pageRequestDto.getSize());
        rttr.addAttribute("type", pageRequestDto.getType());
        rttr.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:read";
    }

    @GetMapping("/modify")
    public void getDinerModify(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, @RequestParam Long did,
            Model model) {
        log.info("get diner modify 페이지 요청");
        DinerDto dinerDto = dinerService.getDinerDetail(did);
        List<DinerCategoryDto> categories = dinerService.getCategoryList();

        model.addAttribute("categories", categories);
        model.addAttribute("dinerDto", dinerDto);
    }

    @PostMapping("/modify")
    public String postDinerModify(DinerDto dinerDto, @ModelAttribute("requestDto") PageRequestDto pageRequestDto,
            RedirectAttributes rttr) {
        log.info("식당 정보 수정 {}", dinerDto);

        Long did = dinerService.updateDiner(dinerDto);

        rttr.addAttribute("did", did);
        rttr.addAttribute("page", 1);
        rttr.addAttribute("size", pageRequestDto.getSize());
        rttr.addAttribute("type", pageRequestDto.getType());
        rttr.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:/diner/read";
    }

    @PostMapping("/delete")
    public String postDinerDelete(@RequestParam Long did, @ModelAttribute("requestDto") PageRequestDto pageRequestDto,
            RedirectAttributes rttr) {
        log.info("식당 삭제 {}", did);

        dinerService.deleteDiner(did);

        rttr.addAttribute("page", pageRequestDto.getPage());
        rttr.addAttribute("size", pageRequestDto.getSize());
        rttr.addAttribute("type", pageRequestDto.getType());
        rttr.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:/diner/list";
    }

    @GetMapping("/review")
    public void getDinerReview() {
        log.info("get diner review 페이지 요청");
    }

    private String makeFolder(String uploadPageName, String uploadId) {
        String dirStr = uploadPageName + "/" + uploadId;

        log.info(dirStr + " 폴더 생성");
        File dirs = new File(uploadPageName, uploadId);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }

        return dirStr;
    }
}
