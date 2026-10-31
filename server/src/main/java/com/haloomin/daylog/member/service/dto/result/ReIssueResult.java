package com.haloomin.daylog.member.service.dto.result;

import lombok.Builder;

@Builder
public record ReIssueResult(
        String accessToken,
        String refreshToken
) {
}
