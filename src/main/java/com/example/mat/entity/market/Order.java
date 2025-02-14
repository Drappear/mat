package com.example.mat.entity.market;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.constant.OrderStatus;
import com.example.mat.entity.shin.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "mat_order")
@Entity
public class Order extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long oid;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    private String orderUid; // 주문번호

    // 주문자 이름
    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    private String zipcode;

    private String addr;

    private String detailAddr;

    private String email;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

}
