package com.example.mat.entity.won;

import com.example.mat.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

// 게시판의 카테고리 엔티티 클래스
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
@Entity
public class BoardCategory extends BaseEntity {

    // 카테고리 고유 번호
    @Id
    @SequenceGenerator(name = "board_category_seq_gen", sequenceName = "board_category_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_category_seq_gen")
    private Long boardCNo;

    // 카테고리 이름
    @Column(nullable = false)
    private String name;
}