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
@ToString(exclude = { "image", "boardCategory" })
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mat_board_seq_gen")
    @SequenceGenerator(name = "mat_board_seq_gen", sequenceName = "mat_board_seq", allocationSize = 1, initialValue = 1)
    private Long bno;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private Long viewCount = 0L;

    @Column(nullable = false)
    private String nick = "Anonymous"; // 기본값 추가

    // BoardCategory와의 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BoardCategory boardCategory;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private BoardImage image;
}
