package com.haloomin.daylog.config.security.jwt.dto;

import java.time.Instant;

public record IssuedRefreshToken(
        String token,
        String jti,
        String familyId,
        Instant expiresAt
) {
}
