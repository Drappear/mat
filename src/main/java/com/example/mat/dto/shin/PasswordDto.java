package com.example.mat.dto.shin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordDto {
    private String email;
    private String currentPassword;
    private String newPassword;
}
