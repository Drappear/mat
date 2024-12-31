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

import com.example.mat.entity.Image;
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
  private ImageRepository imageRepository;

  @Autowired
  private DinerCategoryRepository dinerCategoryRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

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

  // 식당 카테고리
  @Test
  public void categoryInsertTest() {
    dinerCategoryRepository.save(DinerCategory.builder().name("한식").build());
    dinerCategoryRepository.save(DinerCategory.builder().name("양식").build());
    dinerCategoryRepository.save(DinerCategory.builder().name("중식").build());
    dinerCategoryRepository.save(DinerCategory.builder().name("일식").build());
    dinerCategoryRepository.save(DinerCategory.builder().name("카페&디저트").build());
    dinerCategoryRepository.save(DinerCategory.builder().name("퓨전&기타").build());
  }

  // 식당 정보 등록
  @Test
  public void dinerInsertTest() {
    IntStream.rangeClosed(1, 10).forEach(i -> {
      int num = (int) (Math.random() * 6) + 1;
      Diner diner = Diner.builder()
          .address("서울 종로구")
          .addressDetail(111 * i + "-" + 111 * i)
          .content("안녕하세요 " + i + "번 식당 소개글입니다.")
          .menu("메뉴1 : 000000, 메뉴2 : 000000, 메뉴3 : 000000")
          .name(i + "번 식당")
          .phone(111 * i + "-" + 1111 * i + "-" + 1111 * i)
          .regNum(111 * i + "-" + 11 * i + "-" + 11111 * i)
          .workTime("오전 00:00 ~ 00:00, 점심시간 00:00 ~ 00:00, 오후 00:00 ~ 00:00")
          .viewCount(0L)
          .dinerCategory(DinerCategory.builder().dcid(Long.valueOf(num)).build())
          .build();

      dinerRepository.save(diner);
    });
  }

  // 식당 사진
  @Test
  public void insertDinerImageTest() {
    IntStream.rangeClosed(1, 10).forEach(i -> {

      Image image1 = Image.builder()
          .imgCate(1)
          .imgName("diner" + i + ".jpg")
          .path("2024/12/31")
          .uuid("d" + i)
          .diner(Diner.builder().did(Long.valueOf(i)).build())
          .build();
      Image image2 = Image.builder()
          .imgCate(1)
          .imgName("food" + i + ".jpg")
          .path("2024/12/31")
          .uuid("f" + i)
          .diner(Diner.builder().did(Long.valueOf(i)).build())
          .build();
      Image image3 = Image.builder()
          .imgCate(1)
          .imgName("food" + 11 * i + ".jpg")
          .path("2024/12/31")
          .uuid("f" + 11 * i)
          .diner(Diner.builder().did(Long.valueOf(i)).build())
          .build();

      imageRepository.save(image3);
      imageRepository.save(image2);
      imageRepository.save(image1);
    });
  }

}
