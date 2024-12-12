package com.example.mat.entity.market;

import com.example.mat.entity.BaseEntity;

public class Cart extends BaseEntity {

    private Long cartid;

    private int quantity;

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

}
