package com.haloomin.daylog.config.security.jwt;

import com.haloomin.daylog.config.security.jwt.dto.IssuedRefreshToken;
import com.haloomin.daylog.config.security.jwt.dto.ParsedRefreshToken;
import com.haloomin.daylog.member.entity.MemberAccount;
import com.haloomin.daylog.member.exception.InvalidRefreshTokenException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;
    private final JwtDecoder refreshTokenDecoder;

    public JwtTokenProvider(JwtEncoder jwtEncoder, JwtProperties jwtProperties, SecretKey jwtSecretKey) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(JwtConsts.ISSUER));
        this.refreshTokenDecoder = decoder;
    }

    /**
     * accessToken 생성
     *
     * @param account 계정
     * @param now     현재 UTC 시간
     * @return accessToken
     */
    public String createAccessToken(MemberAccount account, Instant now) {
        // access 토큰 생성
        JwtClaimsSet claims = baseClaims(
                account,
                JwtConsts.TYPE_ACCESS,
                now,
                jwtProperties.accessTokenExpireTime()
        ).build();
        return encode(claims);
    }

    /**
     * refreshToken 생성
     *
     * @param account  계정
     * @param familyId token familyId
     * @param now      현재 UTC 시간
     * @return refreshToken
     */
    public IssuedRefreshToken createRefreshToken(
            MemberAccount account,
            String familyId,
            Instant now
    ) {
        // jti: JWT ID 생성
        String jti = UUID.randomUUID().toString();
        // 만료 시간 지정
        Instant expiresAt = now.plusMillis(jwtProperties.refreshTokenExpireTime());
        // refreshToken 생성, DB 저장 데이터 지정
        JwtClaimsSet claims = baseClaims(
                account,
                JwtConsts.TYPE_REFRESH,
                now,
                jwtProperties.refreshTokenExpireTime()
        )
                .id(jti)
                .claim(JwtConsts.CLAIM_FAMILY_ID, familyId)
                .build();
        return new IssuedRefreshToken(encode(claims), jti, familyId, expiresAt);
    }

    /**
     * refreshToken 파싱
     *
     * @param refreshToken 파싱 대상 refreshToken
     * @return 파싱된 refreshToken
     */
    public ParsedRefreshToken parseRefreshToken(String refreshToken) {
        Jwt jwt;
        // refreshToken 디코드
        try {
            jwt = refreshTokenDecoder.decode(refreshToken);
        } catch (JwtException e) {
            throw new InvalidRefreshTokenException();
        }
        // type이 refreshToken이 아닐경우
        if (!JwtConsts.TYPE_REFRESH.equals(jwt.getClaimAsString(JwtConsts.CLAIM_TYPE))) {
            throw new InvalidRefreshTokenException();
        }
        // jti가 없을 경우
        if (jwt.getId() == null) {
            throw new InvalidRefreshTokenException();
        }
        // familyID가 없을경우
        if (jwt.getClaimAsString(JwtConsts.CLAIM_FAMILY_ID) == null) {
            throw new InvalidRefreshTokenException();
        }
        // 파싱된 refreshToken 리턴
        return new ParsedRefreshToken(
                jwt.getId(),
                jwt.getClaimAsString(JwtConsts.CLAIM_FAMILY_ID),
                jwt.getSubject()
        );
    }

    /**
     * 토큰 베이스 생성
     *
     * @param account      계정
     * @param type         토큰 종류
     * @param now          현재 UTC 시간
     * @param expireMillis 토큰 만료 시간 설정
     * @return 토큰 베이스
     */
    private JwtClaimsSet.Builder baseClaims(MemberAccount account, String type, Instant now, long expireMillis) {
        return JwtClaimsSet.builder()
                .issuer(JwtConsts.ISSUER)
                .issuedAt(now)
                .expiresAt(now.plusMillis(expireMillis))
                .subject(account.getEmail())
                .claim(JwtConsts.CLAIM_ROLE, account.getRole().name())
                .claim(JwtConsts.CLAIM_TYPE, type);
    }

    /**
     * JWT 서명
     *
     * @param claims
     * @return 토큰 반환
     */
    private String encode(JwtClaimsSet claims) {
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims
        )).getTokenValue();
    }
}
