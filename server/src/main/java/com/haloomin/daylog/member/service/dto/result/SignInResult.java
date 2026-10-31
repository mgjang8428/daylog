package com.haloomin.daylog.member.service.dto.result;

import lombok.Builder;

@Builder
public record SignInResult(
        String accessToken,
        String refreshToken
) {
}
