package com.haloomin.daylog.api.v1.member.dto.response;

import com.haloomin.daylog.member.service.dto.result.SignInResult;
import lombok.Builder;

@Builder
public record AccountSignInResponseV1(
        String accessToken,
        String refreshToken
) {
    public static AccountSignInResponseV1 from(SignInResult signInResult) {
        return AccountSignInResponseV1.builder()
                .accessToken(signInResult.accessToken())
                .refreshToken(signInResult.refreshToken())
                .build();
    }
}
