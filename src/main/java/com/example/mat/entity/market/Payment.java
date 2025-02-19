package com.example.mat.entity.market;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.constant.PaymentStatus;
import com.example.mat.entity.shin.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mat_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "payment_uid", unique = true, nullable = false)
    private String paymentUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pid")
    private Product product;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
