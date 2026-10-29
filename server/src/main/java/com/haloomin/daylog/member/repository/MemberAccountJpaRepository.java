package com.haloomin.daylog.member.repository;

import com.haloomin.daylog.member.entity.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAccountJpaRepository extends JpaRepository<MemberAccount, Long> {
}
