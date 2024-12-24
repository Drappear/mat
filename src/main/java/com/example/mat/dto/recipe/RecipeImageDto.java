package com.example.mat.dto.recipe;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecipeImageDto {

  private Long rInum;

  private String uuid;

  private String imgName;

  private String path;

  // TODO: rno 가져오기 -> Recipe 에서
  private Long rno;

  private LocalDateTime regDate;
  private LocalDateTime updateDate;

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

  //TODO: 썸네일 서버저장 및 DB저장 필요없다 // 썸네일 경로
  // public String getThumbImageURL() {
  //   String fullPath = "";
  //   try {
  //     fullPath = URLEncoder.encode(path + File.separator + "s_" + uuid + "_" + imgName, "utf-8");
  //   } catch (UnsupportedEncodingException e) {
  //     e.printStackTrace();
  //   }
  //   return fullPath;
  // }
}