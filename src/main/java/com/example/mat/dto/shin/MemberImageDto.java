package com.example.mat.dto.shin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberImageDto {

    private Long inum;

    private String uuid;

    private String imgName;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private Long mid; // 회원 번호

    // 원본 이미지 경로
    public String getImageURL() {
        String fullPath = "";
        try {
            fullPath = URLEncoder.encode(uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fullPath;
    }
}