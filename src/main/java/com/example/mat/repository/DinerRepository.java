package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.diner.Diner;

public interface DinerRepository extends JpaRepository<Diner, Long> {

}
