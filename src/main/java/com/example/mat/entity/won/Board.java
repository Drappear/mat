// Board 엔티티
package com.example.mat.entity.won;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50, message = "제목은 최대 50자까지 작성할 수 있습니다.")
    private String title;

    @Column(nullable = false)
    @Size(max = 1300, message = "내용은 최대 1300자까지 작성할 수 있습니다.")
    private String content;

    @Column(nullable = true)
    private Long viewCount = 0L;

    @Column(nullable = false)
    private String nick = "Anonymous"; // 기본값 추가

    // BoardCategory와의 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BoardCategory boardCategory;

    // BoardImage와의 연관 관계 (이미지 필수 아님)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = true) // 이미지가 선택사항이므로 nullable = true
    private BoardImage image;
}