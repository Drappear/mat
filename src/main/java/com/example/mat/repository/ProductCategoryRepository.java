package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.market.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

}
