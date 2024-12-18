package com.example.mat.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mat.entity.diner.Diner;

@SpringBootTest
public class DinerRepositoryTest {
    @Autowired
    private DinerRepository dinerRepository;

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
}
