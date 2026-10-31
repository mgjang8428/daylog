package com.haloomin.daylog.api.v1.member.dto.request;

import com.haloomin.daylog.member.service.dto.command.SignOutCommand;
import jakarta.validation.constraints.NotBlank;

public record AccountSignOutRequestV1(
        @NotBlank
        String refreshToken
) {
    public SignOutCommand toCommand() {
        return SignOutCommand.builder()
                .refreshToken(refreshToken)
                .build();
    }
}
