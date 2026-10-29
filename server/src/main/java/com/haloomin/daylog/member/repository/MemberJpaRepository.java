package com.haloomin.daylog.member.repository;

import com.haloomin.daylog.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
