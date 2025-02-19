package com.example.mat.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRequestDto {

    private String selectedCartItemIds;

    private String selectedQuantities;

    private String recipientName;

    private String phoneNumber;

    private String email;
    private String zipcode;

    private String addr;

    private String detailAddr;

}
