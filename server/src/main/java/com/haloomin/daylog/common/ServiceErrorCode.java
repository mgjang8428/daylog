package com.haloomin.daylog.common;

import lombok.Getter;

@Getter
public enum ServiceErrorCode {
    // 미정의 오류
    E000000("정의되지않은 오류."),

    // 공통 오류
    E000001("DTO Validate 실패 오류"),
    E000002("이미 삭제된 데이터입니다."),
    E000003("삭제되지 않은 데이터입니다."),

    // Member 관련 오류
    E100001("중복된 이메일입니다."),
    E100002("중복된 닉네임입니다."),
    E100003("이메일 또는 비밀번호가 일치하지 않습니다."),
    E100004("잘못된 RefreshToken 입니다."),
    E100005("이메일은 대문자 불가입니다."),
    E100006("잘못된 타임존입니다."),
    E100007("비밀번호와 비밀번호확인이 일치하지 않습니다."),
    ;

    private final String message;

    ServiceErrorCode(String message) {
        this.message = message;
    }
}
