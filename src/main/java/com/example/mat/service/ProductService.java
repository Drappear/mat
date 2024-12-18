package com.example.mat.service;

import java.util.List;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.market.ProductCategoryDto;
import com.example.mat.dto.market.ProductDto;
import com.example.mat.entity.market.Product;
import com.example.mat.entity.market.ProductCategory;

public interface ProductService {

    ProductDto getRow(Long pid);

    PageResultDto<ProductDto, Product> getList(PageRequestDto requestDto);

    List<ProductCategoryDto> getProductCateList();

    // dto > entity
    public default Product dtoToEntity(ProductDto dto) {
        return Product.builder()
                .pid(dto.getPid())
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .productDetail(dto.getProductDetail())
                .productCategory(ProductCategory.builder().cateid(Long.parseLong(dto.getCatename())).build())
                .build();

    }

    // entity > dto
    public default ProductDto entityToDto(Product product) {
        return ProductDto.builder()
                .pid(product.getPid())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .productDetail(product.getProductDetail())
                .regDate(product.getRegDate())
                .updateDate(product.getUpdateDate())
                .build();
    }

}
