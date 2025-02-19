package com.example.mat.entity.market;

import java.util.ArrayList;
import java.util.List;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.constant.OrderStatus;
import com.example.mat.entity.shin.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Column(unique = true)
    private String orderUid; // 주문 unique

    // 주문자 이름
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

    public void addOrderItem(OrderItem orderItem) {
        if (this.orderItems == null) { // ✅ null 체크 후 초기화
            this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(orderItem);
        orderItem.setOrder(this); // ✅ OrderItem의 order 필드도 설정
    }

}
