package com.example.mat.entity.won;

import com.example.mat.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 게시물(Post) 엔티티 클래스
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "mat_board") // 테이블 이름 매핑
public class Board extends BaseEntity {

    @Id
    @SequenceGenerator(name = "mat_board_seq_gen", sequenceName = "mat_board_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mat_board_seq_gen")
    private Long bno; // 게시물 번호 (Primary Key)

    @Column(nullable = false, length = 100)
    private String title; // 게시물 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시물 내용 (긴 텍스트 처리)

    @Column(nullable = false)
    private Long viewCount; // 게시물 조회수

    @Column(nullable = false)
    private String boardCategory; // 게시판 카테고리
}