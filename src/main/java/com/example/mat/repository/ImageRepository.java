package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
