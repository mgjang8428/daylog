package com.haloomin.daylog.member.service.dto.command;

import lombok.Builder;

@Builder
public record SignupCommand(
        String email,
        String password,
        String passwordConfirm,
        String nickname,
        String timezone
) {
}
