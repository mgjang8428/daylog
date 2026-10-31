package com.haloomin.daylog.api.v1.member.dto.request;

import com.haloomin.daylog.member.service.dto.command.SignupCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountSignupRequestV1(
        @Email @NotBlank @Size(min = 5, max = 254)
        String email,
        @NotBlank @Size(min = 8, max = 64)
        String password,
        @NotBlank @Size(min = 8, max = 64)
        String passwordConfirm,
        @NotBlank @Size(min = 2, max = 30)
        String nickname,
        @NotBlank
        String timezone
) {

    public SignupCommand toCommand() {
        return SignupCommand.builder()
                .email(email)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .nickname(nickname)
                .timezone(timezone)
                .build();
    }
}
