package com.example.mat.dto.won;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 게시판 이미지 DTO 클래스
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardImageDto {

<<<<<<< HEAD
    // 이미지 고유 번호
    private Long boardINo;

    // 이미지 UUID
    private String uuid;

    // 이미지 파일 이름
    private String imgName;

    // 이미지 저장 경로
    private String path;

    // 게시글 고유 번호
    private Long bno;

    // 이미지 등록 시간
    private LocalDateTime regDate;

    // 이미지 수정 시간
    private LocalDateTime updateDate;

    /**
     * 이미지의 전체 URL 경로를 반환.
     * 
     * @return 이미지 URL
     */
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
=======
  // 이미지 고유 번호
  private Long boardINo;

  // 이미지 UUID
  private String uuid;

  // 이미지 파일 이름
  private String imgName;

  // 이미지 저장 경로
  private String path;

  // 게시글 고유 번호
  private Long bno;

  // 이미지 등록 시간
  private LocalDateTime regDate;

  // 이미지 수정 시간
  private LocalDateTime updateDate;

  /**
   * 이미지의 전체 URL 경로를 반환.
   * 
   * @return 이미지 URL
   */
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
>>>>>>> 86c8cc0c6022911626db3c215fc316a3a0f5ded7
