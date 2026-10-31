package com.haloomin.daylog.api.v1.member.dto.request;

import com.haloomin.daylog.member.service.dto.command.SignInCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AccountSignInRequestV1(
        @Email @NotBlank
        String email,
        @NotBlank
        String password
) {
    public SignInCommand toCommand() {
        return SignInCommand.builder()
                .email(email)
                .password(password)
                .build();
    }
}
