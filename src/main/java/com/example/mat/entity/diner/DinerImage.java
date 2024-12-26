package com.example.mat.entity.diner;

import com.example.mat.entity.BaseEntity;

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
@ToString(exclude = { "diner", "dinerReview" })
@Setter
@Getter
@Entity
public class DinerImage extends BaseEntity {
    @SequenceGenerator(name = "diner_img_seq_gen", sequenceName = "diner_img_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diner_img_seq_gen")
    @Id
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    private Long imgCate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Diner diner;

    @ManyToOne(fetch = FetchType.LAZY)
    private DinerReview dinerReview;

}
