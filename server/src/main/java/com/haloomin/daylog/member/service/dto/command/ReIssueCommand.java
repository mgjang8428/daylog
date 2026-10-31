package com.haloomin.daylog.member.service.dto.command;

import lombok.Builder;

@Builder
public record ReIssueCommand(
        String refreshToken
) {
}
