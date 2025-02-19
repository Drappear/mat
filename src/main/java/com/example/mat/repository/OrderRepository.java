package com.example.mat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mat.entity.market.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

        @Query("select o from Order o" +
                        " left join fetch o.payment p" +
                        " left join fetch o.member m" +
                        " where o.orderUid = :orderUid")
        Optional<Order> findOrderAndPaymentAndMember(String orderUid);

        @Query("select o from Order o" +
                        " left join fetch o.payment p" +
                        " where o.orderUid = :orderUid")
        Optional<Order> findOrderAndPayment(String orderUid);

        Optional<Order> findByOrderUid(String orderUid);

}
