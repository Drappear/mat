package com.example.mat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mat.entity.market.Order;
import com.example.mat.entity.market.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.product WHERE oi.order.oid = :orderId")
    List<OrderItem> findItemsByOrderId(@Param("orderId") Long orderId);

}
