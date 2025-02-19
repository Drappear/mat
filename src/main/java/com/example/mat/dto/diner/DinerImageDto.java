package com.example.mat.dto.diner;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DinerImageDto {
    private Long inum;
    private String path;

    private Long did;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    // 원본 경로
    public String getImageURL() {
        String fullPath = "";
        try {
            fullPath = URLEncoder.encode(path + File.separator, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fullPath;
    }

    public String getReviewImageURL() {
        String fullPath = "";
        try {
            fullPath = URLEncoder.encode(path + File.separator, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fullPath;
    }
}
