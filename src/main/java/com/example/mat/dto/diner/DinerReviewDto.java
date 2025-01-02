package com.example.mat.dto.diner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class DinerReviewDto {
    private Long rvid;
    private String content;
    private int tasteScore;
    private int priceScore;
    private int serviceScore;

    // Diner
    private Long did;

    // Member
    private Long mid;
    private String nickname;

    @Builder.Default
    private List<DinerImageDto> dinerImageDtos = new ArrayList<>();

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DinerReviewDto) {

            DinerReviewDto dto = (DinerReviewDto) obj;

            if (this.rvid == dto.getRvid()) {
                return true;
            }
            return false;
        }
        return false;
    }
}
