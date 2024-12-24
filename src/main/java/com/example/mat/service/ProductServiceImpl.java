package com.example.mat.service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
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
        
        Pageable pageable = requestDto.getPageable(Sort.by("pid").descending());
        Page<Product> result = productRepository.findAll(pageable);

        Function<Product, ProductDto> fn = (product -> entityToDto(product));

        return new PageResultDto<>(result, fn);
    }

    @Override
    public List<ProductCategoryDto> getProductCateList(Long cateid) {
      
    
        return null;
    }

}
