package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mat.entity.market.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
