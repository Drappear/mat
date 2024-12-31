package com.example.mat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.dto.diner.DinerReviewDto;
import com.example.mat.service.DinerReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Log4j2
@RequestMapping("/review")
@RestController
public class DinerReviewController {
    private final DinerReviewService dinerReviewService;

    @PostMapping("/{did}")
    public Long postDinerReview(@RequestBody DinerReviewDto dinerReviewDto) {
        log.info("리뷰 등록 {}", dinerReviewDto);
        return dinerReviewService.insertReview(dinerReviewDto);
    }

    @GetMapping("/{did}/all")
    public PageResultDto<List<DinerReviewDto>, Object[]> getReviewList(@PathVariable Long did,
            @ModelAttribute("reviewRequestDto") PageRequestDto pageRequestDto, Model model) {
        log.info("리뷰 리스트 요청 {}", did);

        PageResultDto<List<DinerReviewDto>, Object[]> review = dinerReviewService.getDinerReviews(pageRequestDto, did);
        log.info("리뷰 리스트 {}", review);
        model.addAttribute("review", review);

        return review;
    }
}
