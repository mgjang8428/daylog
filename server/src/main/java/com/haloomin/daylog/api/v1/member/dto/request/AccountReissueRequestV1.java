package com.haloomin.daylog.api.v1.member.dto.request;

import com.haloomin.daylog.member.service.dto.command.ReIssueCommand;
import jakarta.validation.constraints.NotBlank;

public record AccountReissueRequestV1(
        @NotBlank
        String refreshToken
) {
    public ReIssueCommand toCommand() {
        return ReIssueCommand.builder()
                .refreshToken(refreshToken)
                .build();
    }
}
