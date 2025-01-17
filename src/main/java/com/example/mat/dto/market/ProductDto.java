package com.example.mat.dto.market;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

}
