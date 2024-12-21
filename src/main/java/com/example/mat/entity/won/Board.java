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
    private String nickname = "Anonymous";

    // BoardCategory와의 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false) // 외래 키로 CATEGORY_ID 사용
    private BoardCategory boardCategory;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private BoardImage image;
<<<<<<< HEAD
}
=======
}
>>>>>>> 86c8cc0c6022911626db3c215fc316a3a0f5ded7
