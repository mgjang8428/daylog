package com.haloomin.daylog.member.repository;

import com.haloomin.daylog.member.entity.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAccountJpaRepository extends JpaRepository<MemberAccount, Long> {

    /**
     * 이메일로 사용자 계정 찾기
     * @param email
     * @return
     */
    Optional<MemberAccount> findByEmail(String email);

    /**
     * 삭제처리 되지 않은 계정찾기
     * @param email
     * @return
     */
    Optional<MemberAccount> findByEmailAndDeletedAtIsNull(String email);

    /**
     * 중복 이메일 검사
     * @param email
     * @return
     */
    boolean existsByEmail(String email);
}
