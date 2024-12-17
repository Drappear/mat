package com.example.mat.entity.won;

import com.example.mat.entity.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "mat_board")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Board extends BaseEntity {

    @Id
    @SequenceGenerator(name = "mat_board_seq_gen", sequenceName = "mat_board_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mat_board_seq_gen")
    private Long bno;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private String boardCategory;
}
