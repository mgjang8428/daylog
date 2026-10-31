package com.haloomin.daylog.api.v1.member.dto.response;

import com.haloomin.daylog.member.service.dto.result.ReIssueResult;
import lombok.Builder;

@Builder
public record AccountReissueResponseV1(
        String accessToken,
        String refreshToken
) {
    public static AccountReissueResponseV1 from(ReIssueResult reIssueResult) {
        return AccountReissueResponseV1.builder()
                .accessToken(reIssueResult.accessToken())
                .refreshToken(reIssueResult.refreshToken())
                .build();
    }
}