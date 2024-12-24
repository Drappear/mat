package com.example.mat.repository.diner;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.diner.DinerReview;

public interface DinerReviewRepository extends JpaRepository<DinerReview, Long> {

}
