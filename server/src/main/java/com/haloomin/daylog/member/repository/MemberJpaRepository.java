package com.haloomin.daylog.member.repository;

import com.haloomin.daylog.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    /**
     * 닉네임 중복 방지
     *
     * @param nickname 닉네임
     * @return 중복 여부
     */
    boolean existsByNickname(String nickname);
}
