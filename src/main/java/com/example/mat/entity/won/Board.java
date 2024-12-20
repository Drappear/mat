package com.example.mat.entity.won;

import jakarta.persistence.*;
import lombok.*;
import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.shin.Member;

import java.util.List;

// 게시판 엔티티 클래스
@Entity
@Table(name = "mat_board")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = { "images", "member" })
public class Board extends BaseEntity {

    // 게시글 고유번호
    @Id
    @SequenceGenerator(name = "mat_board_seq_gen", sequenceName = "mat_board_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mat_board_seq_gen")
    private Long bno;

    // 게시글 제목
    @Column(nullable = false)
    private String title;

    // 게시글 내용
    @Column(nullable = false)
    private String content;

    // 게시글 조회수
    // 초기 값은 0이며, 게시글 조회 시 증가
    @Column(nullable = false)
    private Long viewCount = 0L;

    // 게시판 카테고리
    @Column(nullable = false)
    private String boardCategory;

    // 메인 이미지 경로
    // 게시글의 대표 이미지를 저장합니다.

    @Column
    private String mainImage;

    // 작성자 정보
    // Member 엔티티와 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 게시글에 포함된 이미지 목록
    // BoardImage 엔티티와 일대다 관계
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> images;
}