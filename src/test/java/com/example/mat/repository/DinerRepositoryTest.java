package com.example.mat.repository;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerCategory;

@SpringBootTest
public class DinerRepositoryTest {
    @Autowired
    private DinerRepository dinerRepository;

    @Autowired
    private DinerCategoryRepository dinerCategoryRepository;

    @Test
    public void createDinerTest() {
        Diner diner = Diner.builder()
                .name("식당1")
                .content("등록 테스트")
                .address("서울 종로구")
                .phone("012-3456-7890")
                .regNum("1234-5678")
                .viewCount(0L)
                .build();

        dinerRepository.save(diner);
    }

    @Test
    public void categoryInsertTest() {
      dinerCategoryRepository.save(DinerCategory.builder().name("한식").build());
      dinerCategoryRepository.save(DinerCategory.builder().name("양식").build());
      dinerCategoryRepository.save(DinerCategory.builder().name("중식").build());
      dinerCategoryRepository.save(DinerCategory.builder().name("일식").build());
      dinerCategoryRepository.save(DinerCategory.builder().name("카페&디저트").build());
      dinerCategoryRepository.save(DinerCategory.builder().name("퓨전&기타").build());
    }

}
