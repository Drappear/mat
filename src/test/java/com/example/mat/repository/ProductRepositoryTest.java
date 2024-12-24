package com.example.mat.repository;

import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mat.entity.market.Product;
import com.example.mat.entity.market.ProductCategory;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void testInsertProductCategory() {

        LongStream.rangeClosed(1, 10).forEach(i -> {
            ProductCategory productCategory = ProductCategory.builder()
                    .cateid(i)
                    .build();
            productCategoryRepository.save(productCategory);
        });

    }

    @Test
    public void testGetProduct(){
        // Product R 테스트
        Product product = productRepository.findById(1L).get();
        System.out.println(product);

    }




}
