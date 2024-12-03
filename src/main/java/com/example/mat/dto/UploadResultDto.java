package com.example.mat.dto;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadResultDto { // TODO: DB에 저장할 업로드된파일들
  // uuid, fileName, floderPath

  private String uuid;
  private String fileName; // 원본파일명
  private String folderPath; // 년/월/일

  public String getThumbImageURL() {
    // TODO: 썸네일 이미지 경로 리턴을 위한 메소드
    String fullPath = "";
    try {
      fullPath = URLEncoder.encode(folderPath + File.separator + "s_" + uuid + "_" + fileName, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return fullPath;
  }

  public String getImageURL() {
    // TODO: 원본 이미지 경로 리턴을 위한 메소드
    String fullPath = "";
    try {
      fullPath = URLEncoder.encode(folderPath + File.separator + uuid + "_" + fileName, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return fullPath;
  }
}
