package com.example.mat.entity.market;

import com.example.mat.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "mat_cartitem")
@Entity
public class CartItem extends BaseEntity{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="cartitemid")
    private Long cartItemId;

    private int quantity;
    
    // 하나의 카트에 여러 아이템 담을 수 있다
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    // // 상품 수량 증가
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    // 상품 수량 수정
    public void updateQuantity(int quantity){
        this.quantity = quantity;
    }
    
}
