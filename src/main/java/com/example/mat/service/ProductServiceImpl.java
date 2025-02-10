package com.example.mat.service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.market.ProductCategoryDto;
import com.example.mat.dto.market.ProductDto;
import com.example.mat.entity.market.Product;
import com.example.mat.repository.ProductCategoryRepository;
import com.example.mat.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductDto getRow(Long pid) {

        return entityToDto(productRepository.findById(pid).get());
    }

    @Override
    public PageResultDto<ProductDto, Product> getList(PageRequestDto requestDto) {
        // 강제로 기본 크기 9 적용
        int size = 9;

        // 페이지 번호 기본값 설정 (page 값이 1 미만일 경우)
        int page = (requestDto.getPage() > 0) ? requestDto.getPage() - 1 : 0;

        // Pageable 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by("pid").descending());

        // 데이터 조회
        Page<Product> result = productRepository.findAll(pageable);

        // Entity -> DTO 변환 함수
        Function<Product, ProductDto> fn = (product -> entityToDto(product));

        // PageResultDto 반환
        return new PageResultDto<>(result, fn);
                
                // Pageable pageable = requestDto.getPageable(Sort.by("pid").descending());
                // Page<Product> result = productRepository.findAll(pageable);
                // Function<Product, ProductDto> fn = (product -> entityToDto(product));
                // return new PageResultDto<>(result, fn);
        }

        @Override
        public PageResultDto<ProductDto, Product> getProductsByCategory(Long cateid, PageRequestDto requestDto) {
            int page = (requestDto.getPage() > 0) ? requestDto.getPage() - 1 : 0;
            int size = 9; // 한 페이지에 9개씩 표시
        
            // Pageable 객체 생성
            Pageable pageable = PageRequest.of(page, size, Sort.by("pid").descending());
        
            // 특정 카테고리에 해당하는 상품 조회 (페이징 포함)
            Page<Product> result = productRepository.findByProductCategory_Cateid(cateid, pageable);
        
            // Entity -> DTO 변환 함수
            Function<Product, ProductDto> fn = this::entityToDto;
        
            // PageResultDto 반환
            return new PageResultDto<>(result, fn);
        }



}
