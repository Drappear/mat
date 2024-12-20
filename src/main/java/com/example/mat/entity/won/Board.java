package com.example.mat.entity.won;

import jakarta.persistence.*;
import lombok.*;
import com.example.mat.entity.BaseEntity;

@Entity
@Table(name = "mat_board")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = { "image" }) // 이미지 필드만 제외
public class Board extends BaseEntity {

    @Id
    @SequenceGenerator(name = "mat_board_seq_gen", sequenceName = "mat_board_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mat_board_seq_gen")
    private Long bno;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column(nullable = false)
    private String boardCategory;

    // 작성자 정보
    // Member 엔티티와 다대일 관계
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "member_id", nullable = false)
    // private Member member;
    @Column(nullable = false)
    private String nickname;

    // 게시글에 포함된 단일 이미지
    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BoardImage image;
}
