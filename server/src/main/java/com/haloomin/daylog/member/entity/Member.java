package com.haloomin.daylog.member.entity;

import com.haloomin.daylog.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String nickname;

    @Column(nullable = false, length = 50)
    private ZoneId timezone;

    @Builder
    public Member(String nickname, ZoneId timezone) {
        this.nickname = nickname;
        this.timezone = timezone;
    }
}
