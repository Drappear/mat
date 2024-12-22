// BoardDto 클래스
package com.example.mat.dto.won;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 게시판 DTO 클래스
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardDto {

    private Long bno; // 게시물 번호

    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title; // 게시물 제목

    @NotBlank(message = "내용은 필수 항목입니다.")
    private String content; // 게시물 내용

    private String nick; // 작성자 닉네임

    private Long viewCount; // 조회수

    private LocalDateTime regDate; // 등록일

    private LocalDateTime updateDate; // 수정일

    private Long categoryId; // 카테고리 ID

    private MultipartFile imageFile; // 업로드된 파일
    private String imageFileName; // 저장된 파일 경로

    public String getContentPreview() {
        if (content == null) {
            return "";
        }
        return content.length() > 100 ? content.substring(0, 100) + "..." : content;
    }

    public String getFormattedRegDate() {
        if (regDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return regDate.format(formatter);
    }
}
