package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.market.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByPid(Long pid);


}
