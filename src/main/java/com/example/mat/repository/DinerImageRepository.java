package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.diner.DinerImage;

public interface DinerImageRepository extends JpaRepository<DinerImage, Long> {
}
