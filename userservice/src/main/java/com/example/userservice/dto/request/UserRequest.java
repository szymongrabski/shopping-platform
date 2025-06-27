package com.example.userservice.dto.request;

import com.example.userservice.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserRequest {
    @Email
    private String email;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @ValidPhoneNumber
    private String phoneNumber;
}
