package com.example.mat.dto.recipe;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecipeStepDto {

  private Long stepNum;

  @NotBlank(message = "내용은 필수 항목입니다.")
  private String content; // 레시피 내용

  private String uuid;

  private String imgName;

  private String path;

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