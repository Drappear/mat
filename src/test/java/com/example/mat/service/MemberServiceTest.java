package com.example.mat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.UpdateMemberDto;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService service;

    @Test
    public void testupdate() {

        UpdateMemberDto updatememberDto = UpdateMemberDto.builder()
                .userid("userid1")
                .detailAddr("1층")
                .email("user1@naver.com")
                .addr("경기도 고양시 일산동구 oo 아파트1번지")
                .tel("010123456111")
                .build();

        service.personalUpdate(updatememberDto);

    }

}
