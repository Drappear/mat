package com.example.mat.dto.shin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.mat.entity.constant.MemberRole;
import com.example.mat.entity.shin.MemberImage;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberDto {
    private Long mid;

    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자만 가능합니다.")
    private String userid;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String username;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String tel;

    private String addr;

    private String detailAddr;

    private String bio;

    private MemberImageDto memberImageDto;
    private MemberRole role;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
