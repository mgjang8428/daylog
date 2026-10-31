package com.haloomin.daylog.config.security.jwt.dto;

public record ParsedRefreshToken(
        String jti,
        String familyId,
        String subject
) {
}
