package com.example.mat.dto.market;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ImageDto {

    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    private int imgCate;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    // 썸네일 경로
    public String getThumbImageURL() {
        String fullPath = "";
        try {
            fullPath = URLEncoder.encode(path + File.separator + "s_" + uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fullPath;
    }

    // 원본 이미지 경로
    public String getImageURL() {
        String fullPath = "";
        try {
            fullPath = URLEncoder.encode(path + File.separator + uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fullPath;
    }

}
