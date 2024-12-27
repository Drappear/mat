package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.Image;
import com.example.mat.repository.diner.DinerImageRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, DinerImageRepository {

}
