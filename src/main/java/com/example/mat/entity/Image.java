package com.example.mat.entity;

import com.example.mat.entity.market.Product;

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
@Getter
@Setter
@ToString
@Entity
public class Image extends BaseEntity {
    // inum(seq), uuid(문자), imgName(문자), path(문자)

    @SequenceGenerator(name = "mat_image_seq_gen", sequenceName = "mat_image_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mat_image_seq_gen")
    @Id
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    private int imgCate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
