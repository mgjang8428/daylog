package com.haloomin.daylog.member.service.impl;

import com.haloomin.daylog.member.entity.MemberAccount;
import com.haloomin.daylog.member.repository.MemberAccountJpaRepository;
import com.haloomin.daylog.member.service.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDetailsServiceImpl implements MemberDetailsService {

    private final MemberAccountJpaRepository memberAccountJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberAccount account = memberAccountJpaRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.withUsername(account.getEmail())
                .password(account.getPassword())
                .roles(account.getRole().name())
                .build();
    }
}
