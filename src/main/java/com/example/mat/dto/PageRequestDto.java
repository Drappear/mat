// 임의로 코드 수정했음!!!

// 주의 !!!

package com.example.mat.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 페이징 및 검색 요청 DTO
 */
@ToString
@Builder
@AllArgsConstructor
@Setter
@Getter
public class PageRequestDto {
  private int page; // 요청 페이지 번호
  private int size; // 한 페이지당 게시물 수
  private String type; // 검색 유형
  private String keyword; // 검색 키워드

  /**
   * 기본 생성자
   * 페이지는 1, 크기는 10으로 초기화됩니다.
   */
  public PageRequestDto() {
    this.page = 1;
    this.size = 10;
  }

  /**
   * 정렬 조건을 사용하여 Pageable 객체 생성
   *
   * @param sort 정렬 조건
   * @return Pageable 객체
   */
  public Pageable getPageable(Sort sort) {
    // 페이지 번호는 0부터 시작하므로, 1보다 작으면 0으로 설정
    int pageIndex = Math.max(page - 1, 0);
    return PageRequest.of(pageIndex, size, sort != null ? sort : Sort.unsorted());
  }

  /**
   * 기본 정렬 없이 Pageable 객체 생성
   *
   * @return Pageable 객체
   */
  public Pageable getPageable() {
    return getPageable(Sort.unsorted());
  }
}
