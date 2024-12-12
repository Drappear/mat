package com.example.mat.entity.market;

import java.util.ArrayList;
import java.util.List;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.Image;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "mat_product")
@Entity
public class Product extends BaseEntity {
    // pid, name, price, quantity, productDetail

    @SequenceGenerator(name = "mat_product_seq_gen", sequenceName = "mat_product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mat_product_seq_gen")
    @Id
    private Long pid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    private int quantity;

    private String productDetail;

    @ManyToOne
    private ProductCategory productCategory;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Image> images = new ArrayList<>();

}
