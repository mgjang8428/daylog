package com.haloomin.daylog.config.security.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtConsts {
    static final String TYPE_ACCESS = "access";
    static final String TYPE_REFRESH = "refresh";
    static final String CLAIM_TYPE = "type";
    static final String CLAIM_ROLE = "role";
    static final String CLAIM_FAMILY_ID = "fid";
    static final String ISSUER = "daylog";
}
