package com.haloomin.daylog.member.entity;

import com.haloomin.daylog.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "member_account_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false)
    private String password;

     @Enumerated(EnumType.STRING)
     private MemberRole role;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    Member member;

    @Builder
    public MemberAccount(String email, String password, MemberRole role, Member member) {
        this.email = email.toLowerCase();
        this.password = password;
        this.role = role;
        this.member = member;
    }
}
