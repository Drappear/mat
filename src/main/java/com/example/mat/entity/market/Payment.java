package com.example.mat.entity.market;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.shin.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "mat_payment")
@Entity
public class Payment extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    @Id
    private Long id; // PK

    @ManyToOne
    private Member member; // 사용자
    
    @ManyToOne
    private Order order;
    
    @ManyToOne
    private Product product;

    private int price; // 상품 가격
    
    private Long totalPrice; // 결제한 총 가격

    @Column(name = "status")
    private Boolean status = true; // 상태
}
