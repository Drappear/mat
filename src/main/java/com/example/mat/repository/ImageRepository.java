package com.example.mat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.Image;
import com.example.mat.repository.diner.DinerImageRepository;
import com.example.mat.repository.diner.DinerReviewImageRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, DinerImageRepository, DinerReviewImageRepository {
  @Query(value = "SELECT * FROM IMAGE i WHERE i.PATH = to_char(sysdate-1, 'yyyy/mm/dd')", nativeQuery = true)
    List<Image> findOldFileAll();
}
