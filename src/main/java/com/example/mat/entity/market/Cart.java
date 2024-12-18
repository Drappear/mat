package com.example.mat.entity.market;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "mat_cart")
@Entity
public class Cart extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long cartid;

    private int quantity; // 카트에 담긴 상품 개수
    
    @OneToOne(fetch = FetchType.LAZY)
    Member member;

    // 멤버에게 카트 부여 
    // 멤버 한명 > 카트 한개
    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }



}
