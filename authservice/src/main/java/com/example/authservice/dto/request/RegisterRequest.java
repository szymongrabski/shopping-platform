package com.example.authservice.dto.request;

import com.example.authservice.validation.Adult;
import com.example.authservice.validation.ValidPassword;
import com.example.authservice.validation.ValidPhoneNumber;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Email
    private String email;

    @ValidPassword
    private String password;

    @NotEmpty
    @Size(min = 3, max = 10)
    private String username;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @ValidPhoneNumber
    private String phoneNumber;

    @Past
    @Adult
    private LocalDate birthDate;
}
