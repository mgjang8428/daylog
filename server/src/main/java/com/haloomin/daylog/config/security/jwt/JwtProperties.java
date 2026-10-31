package com.haloomin.daylog.config.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secretKey,
        long accessTokenExpireTime,
        long refreshTokenExpireTime
) {
}
