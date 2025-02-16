package com.example.mat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.market.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByPid(Long pid);

    // 카테고리 ID로 상품 조회
    Page<Product> findByProductCategory_Cateid(Long cateid, Pageable pageable);


}
