package com.example.mat.repository;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import com.example.mat.entity.constant.MemberRole;
import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerCategory;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.diner.DinerCategoryRepository;
import com.example.mat.repository.diner.DinerRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class DinerRepositoryTest {
  @Autowired
  private DinerRepository dinerRepository;

  @Autowired
  private DinerCategoryRepository dinerCategoryRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

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

  // test Member
  @Commit
  @Transactional
  @Test
  public void testInsert() {

    // 테스트용 일반계정
    Member member = Member.builder()
        .userid("test")
        .nickname("test")
        .password(passwordEncoder.encode("test"))
        .username("test")
        .email("test@test.com")
        .tel("01012345678")
        .addr("test")
        .detailAddr("test")
        .role(MemberRole.MEMBER)
        .build();
    memberRepository.save(member);

    // 테스트용 관리자계정
    Member admin = Member.builder()
        .userid("admin")
        .nickname("admin")
        .password(passwordEncoder.encode("admin"))
        .username("admin")
        .email("admin@admin.com")
        .tel("01012345678")
        .addr("admin")
        .detailAddr("admin")
        .role(MemberRole.ADMIN)
        .build();
    memberRepository.save(admin);

  }

}
