package com.example.mat.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.dto.diner.DinerReviewDto;
import com.example.mat.dto.diner.DinnerReviewTotalDto;
import com.example.mat.entity.Image;
import com.example.mat.entity.diner.DinerReview;
import com.example.mat.service.DinerReviewService;

import jakarta.transaction.Transactional;

@SpringBootTest
public class DinnerImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private DinerReviewService dinerReviewService;

    @Test
    public void list() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Object[]> result = imageRepository.getTotalReviewList(pageRequest, 10L);

    }

    @Transactional
    @Test
    public void servicelist() {

        PageRequestDto pageRequestDto = PageRequestDto.builder().size(10).page(1).build();
        // PageResultDto<List<DinerReviewDto>, Object[]> result =
        // dinerReviewService.getDinerReviews(pageRequestDto, 10L);

        // result.getDtoList().forEach(dto -> System.out.println(dto));

    }

}
