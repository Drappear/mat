package com.example.mat.dto.won;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardListDto {
    private Long bno;
    private String title;
    private Long viewCount;
    private LocalDateTime regDate;
    private String userid;
}
