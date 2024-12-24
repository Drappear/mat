// Board 엔티티
package com.example.mat.entity.won;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.shin.Member;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // Member 엔티티와 연결

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BoardCategory boardCategory;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = true) // 이미지가 선택사항이므로 nullable = true
    private BoardImage image;

    // 기존 member와의 관계를 유지하면서, userid를 간접적으로 접근 가능
    public String getUserid() {
        return member != null ? member.getUserid() : null;
    }
}
