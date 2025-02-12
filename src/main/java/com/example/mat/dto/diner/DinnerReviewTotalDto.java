package com.example.mat.dto.diner;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class DinnerReviewTotalDto {

    // Diner
    private Long did;

    @Builder.Default
    List<DinerReviewDto> dinerReviewDtos = new ArrayList<>();
}
