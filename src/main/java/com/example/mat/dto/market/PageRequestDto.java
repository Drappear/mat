package com.example.mat.dto.market;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//  http://localhost:8080/book/list?page=2&size=20&type=c&keyword=소년

@ToString
@Builder
@AllArgsConstructor
@Setter
@Getter
public class PageRequestDto {
  private int page;
  private int size;

  // 검색
  private String type;
  private String keyword;

  public PageRequestDto() {
    this.page = 1;
    this.size = 10;
  }

  public Pageable getPageable(Sort sort) {
    // 0 : 1 page
    return PageRequest.of(page - 1, size, sort);
  }

}