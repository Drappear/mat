package com.example.mat.repository.diner;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.Image;
import com.example.mat.entity.diner.Diner;

public interface DinerImageRepository {
  // 페이지 나누기, 검색
  Page<Object[]> getTotalDinerList(String type, String keyword, Pageable pageable);

  // 리뷰 목록
  Page<Object[]> getTotalReviewList(Pageable pageable, Long did);

  // 특정 식당 정보 조회
  List<Object[]> getDinerRow(Long did);

  // diner.did 를 이용해 dinerImage제거
  @Modifying
  @Query("DELETE FROM Image di WHERE di.diner = :diner")
  void deleteByDiner(Diner diner);

  
}
