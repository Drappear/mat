package com.example.mat.entity.market;

<<<<<<<HEAD=======

import java.util.ArrayList;
import java.util.List;

>>>>>>>a75399ed05997c06800ff87847bf1af7d7a6002a
import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.constant.OrderStatus;
import com.example.mat.entity.shin.Member;

<<<<<<<HEAD=======
import jakarta.persistence.CascadeType;>>>>>>>a75399ed05997c06800ff87847bf1af7d7a6002a
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;<<<<<<<HEAD=======
import jakarta.persistence.OneToMany;>>>>>>>a75399ed05997c06800ff87847bf1af7d7a6002a
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

<<<<<<< HEAD
    private String orderUid; // 주문번호

    // 주문자 이름
    @Column(nullable = false)
    private String name;
=======
    @Column(unique = true)
    private String orderUid; // 주문 unique

    // 주문자 이름
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;
>>>>>>> a75399ed05997c06800ff87847bf1af7d7a6002a

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
