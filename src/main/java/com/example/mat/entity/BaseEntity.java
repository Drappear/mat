package com.example.mat.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = { AuditingEntityListener.class })
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

  @CreatedDate
  @Column(updatable = false, name = "regdate")
  private LocalDateTime regDate; // 최초 생성 시간

  @LastModifiedDate
  @Column(name = "updatedate") // update_date
  private LocalDateTime updateDate; // 최종 수정 시간
}