package com.haloomin.daylog.member.entity;

import com.haloomin.daylog.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_refresh_token_store_family_id", columnList = "family_id"),
        @Index(name = "idx_refresh_token_store_expires_at", columnList = "expires_at")
})
@Entity
public class RefreshTokenStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refreshtoken_store_id")
    private Long id;

    @Column(length = 36, nullable = false, unique = true)
    private String jti;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(length = 36, nullable = false)
    private String familyId;

    @ManyToOne()
    @JoinColumn(name = "member_account_id", nullable = false)
    private MemberAccount memberAccount;

    @Builder
    public RefreshTokenStore(String jti, Instant expiresAt, String familyId, MemberAccount memberAccount) {
        this.jti = jti;
        this.expiresAt = expiresAt;
        this.familyId = familyId;
        this.memberAccount = memberAccount;
    }
}
