package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.diner.DinerCategory;

public interface DinerCategoryRepository extends JpaRepository<DinerCategory, Long>{
  
}
