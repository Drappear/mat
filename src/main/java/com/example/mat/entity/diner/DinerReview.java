package com.example.mat.entity.diner;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.shin.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "diner", "member", "dinerImage" })
@Setter
@Getter
@Entity
public class DinerReview extends BaseEntity {

    @SequenceGenerator(name = "diner_review_seq_gen", sequenceName = "diner_review_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diner_review_seq_gen")
    @Id
    private Long rvid;

    private String content;
    private int tasteScore;
    private int priceScore;
    private int serviceScore;

    // Diner
    @ManyToOne(fetch = FetchType.LAZY)
    private Diner diner;

    // Member
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // DinerImage
    @ManyToOne(fetch = FetchType.LAZY)
    private DinerImage dinerImage;
}
