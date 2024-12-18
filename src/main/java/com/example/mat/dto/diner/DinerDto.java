package com.example.mat.dto.diner;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DinerDto {
    private Long did;
    private String name;
    private String address;
    private String phone;
    private String content;
    private String menu;
    private String workTime;
    private Long viewCount;
    private String regNum;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
