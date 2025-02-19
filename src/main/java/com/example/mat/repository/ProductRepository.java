package com.example.mat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.mat.entity.market.Product;
import com.example.mat.entity.market.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    Product findByPid(Long pid);

    // 카테고리 ID로 상품 조회
    Page<Product> findByProductCategory_Cateid(Long cateid, Pageable pageable);

    // 상품명 검색 기능 추가
    default Predicate makePredicate(String type, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();
        QProduct qProduct = QProduct.product;

        // 기본 조건: id > 0 (전체 상품 조회)
        builder.and(qProduct.pid.gt(0L));

        if (type == null || keyword == null || keyword.isEmpty()) {
            return builder;
        }

        if (type.equals("n")) { // 상품명 검색
            builder.and(qProduct.name.containsIgnoreCase(keyword));
        } else if (type.equals("c")) { // 카테고리 검색
            builder.and(qProduct.productCategory.name.containsIgnoreCase(keyword));
        }

        return builder;
    }

}
