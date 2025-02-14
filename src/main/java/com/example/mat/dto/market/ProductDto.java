package com.example.mat.dto.market;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDto {

    private Long pid;

    private String name;

    private int price;

    private int quantity;

    private String productDetail;

    private String catename;

    private ProductCategoryDto category;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

}
