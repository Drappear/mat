package com.example.mat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mat.dto.diner.DinerReviewDto;
import com.example.mat.service.DinerReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

}
