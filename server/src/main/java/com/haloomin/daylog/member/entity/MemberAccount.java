package com.haloomin.daylog.member.entity;

import com.haloomin.daylog.common.entity.BaseEntity;

import jakarta.persistence.*;
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
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    Member member;

    @Builder
    public MemberAccount(String email, String password, Member member) {
        this.email = email.toLowerCase();
        this.password = password;
        this.member = member;
    }
}
