package com.example.mat.dto.shin;

import java.time.LocalDateTime;

import com.example.mat.entity.constant.MemberRole;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateMemberDto {
    private Long mid;

    private String userid;

    private String password;

    private String username;

    private String nickname;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String tel;

    private String addr;

    private String detailAddr;

    private MemberRole role;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
