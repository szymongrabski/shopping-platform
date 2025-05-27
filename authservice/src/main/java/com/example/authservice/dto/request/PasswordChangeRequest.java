package com.example.authservice.dto.request;

import com.example.authservice.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {
    @NotBlank
    private String oldPassword;

    @ValidPassword
    private String newPassword;
}
