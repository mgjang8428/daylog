package com.haloomin.daylog.member.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haloomin.daylog.config.security.jwt.JwtTokenProvider;
import com.haloomin.daylog.config.security.jwt.dto.IssuedRefreshToken;
import com.haloomin.daylog.config.security.jwt.dto.ParsedRefreshToken;
import com.haloomin.daylog.member.entity.Member;
import com.haloomin.daylog.member.entity.MemberAccount;
import com.haloomin.daylog.member.entity.MemberRole;
import com.haloomin.daylog.member.entity.RefreshTokenStore;
import com.haloomin.daylog.member.exception.AlreadySignupEmailException;
import com.haloomin.daylog.member.exception.AlreadySignupNicknameException;
import com.haloomin.daylog.member.exception.EmailNotFoundException;
import com.haloomin.daylog.member.exception.InvalidRefreshTokenException;
import com.haloomin.daylog.member.exception.NotAllowedUppercaseEmailException;
import com.haloomin.daylog.member.exception.NotFoundTimeZoneException;
import com.haloomin.daylog.member.exception.PasswordConfirmFailedException;
import com.haloomin.daylog.member.exception.PasswordNotMatchesException;
import com.haloomin.daylog.member.repository.MemberAccountJpaRepository;
import com.haloomin.daylog.member.repository.MemberJpaRepository;
import com.haloomin.daylog.member.repository.RefreshTokenStoreJpaRepository;
import com.haloomin.daylog.member.service.MemberService;
import com.haloomin.daylog.member.service.dto.command.ReIssueCommand;
import com.haloomin.daylog.member.service.dto.command.SignInCommand;
import com.haloomin.daylog.member.service.dto.command.SignOutCommand;
import com.haloomin.daylog.member.service.dto.command.SignupCommand;
import com.haloomin.daylog.member.service.dto.result.ReIssueResult;
import com.haloomin.daylog.member.service.dto.result.SignInResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberJpaRepository memberJpaRepository;
    private final MemberAccountJpaRepository memberAccountJpaRepository;
    private final RefreshTokenStoreJpaRepository refreshTokenStoreJpaRepository;

    /**
     * 사용자 가입
     *
     * @param command 가입정보
     */
    @Override
    @Transactional
    public void signUp(SignupCommand command) {
        // 받은 정보 검증
        validateMemberInfo(command);
        // Member, MemberAccount 생성 및 저장
        Member member = Member.builder()
                .nickname(command.nickname())
                .timezone(ZoneId.of(command.timezone()))
                .build();
        MemberAccount memberAccount = MemberAccount.builder()
                .email(command.email())
                .password(passwordEncoder.encode(command.password()))
                .role(MemberRole.USER)
                .member(member)
                .build();
        memberJpaRepository.save(member);
        memberAccountJpaRepository.save(memberAccount);
    }

    /**
     * Member 정보 검증
     *
     * @param command 가입 정보
     */
    private void validateMemberInfo(SignupCommand command) {
        // email에 대문자 입력 방지
        if (!command.email().equals(command.email().toLowerCase())) {
            throw new NotAllowedUppercaseEmailException();
        }
        // 비밀번호 일치 확인
        if (!command.password().equals(command.passwordConfirm())) {
            throw new PasswordConfirmFailedException();
        }
        // email 중복 확인
        if (memberAccountJpaRepository.existsByEmail(command.email().toLowerCase())) {
            throw new AlreadySignupEmailException();
        }
        // nickname 중복 확인
        if (memberJpaRepository.existsByNickname(command.nickname())) {
            throw new AlreadySignupNicknameException();
        }
        // timezone 존재 확인
        final Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        if (!availableZoneIds.contains(command.timezone())) {
            throw new NotFoundTimeZoneException();
        }
    }

    /**
     * 사용자 로그인
     *
     * @param command Email, password
     * @return accessToken, refreshToken
     */
    @Override
    @Transactional
    public SignInResult signIn(SignInCommand command) {
        // 로그인 검증 및 조회
        MemberAccount memberAccount = findAuthenticatedAccount(command);

        // 토큰 발급
        Instant now = Instant.now();
        String familyId = UUID.randomUUID().toString();
        String accessToken = jwtTokenProvider.createAccessToken(memberAccount, now);
        IssuedRefreshToken refreshToken = jwtTokenProvider.createRefreshToken(memberAccount, familyId, now);

        // RefreshToken 저장
        RefreshTokenStore refreshTokenStore = RefreshTokenStore.builder()
                .jti(refreshToken.jti())
                .familyId(refreshToken.familyId())
                .expiresAt(refreshToken.expiresAt())
                .memberAccount(memberAccount)
                .build();
        refreshTokenStoreJpaRepository.save(refreshTokenStore);

        // return
        return SignInResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.token())
                .build();
    }

    @Override
    @Transactional
    public void signOut(SignOutCommand command) {
        // refreshToken 파싱
        ParsedRefreshToken parsedRefreshToken = jwtTokenProvider.parseRefreshToken(command.refreshToken());

        refreshTokenStoreJpaRepository.findAllByFamilyIdAndDeletedAtIsNull(parsedRefreshToken.familyId())
                .forEach(RefreshTokenStore::softDelete);
    }

    /**
     * 로그인 검증 및 조회
     *
     * @param command 로그인 정보
     * @return 계정
     */
    private MemberAccount findAuthenticatedAccount(SignInCommand command) {
        // 이메일 조회
        MemberAccount memberAccount = memberAccountJpaRepository.findByEmailAndDeletedAtIsNull(command.email())
                .orElseThrow(EmailNotFoundException::new);

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(command.password(), memberAccount.getPassword())) {
            throw new PasswordNotMatchesException();
        }
        return memberAccount;
    }

    /**
     * 사용자 토큰 재발급
     *
     * @param command DTO
     * @return DTO accessToken, refreshToken
     */
    @Override
    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public ReIssueResult reIssue(ReIssueCommand command) {
        // 받은 refreshToken 파싱
        ParsedRefreshToken parsedRefreshToken = jwtTokenProvider.parseRefreshToken(command.refreshToken());
        // 받은 refreshToken 조회
        RefreshTokenStore storedRefreshToken = refreshTokenStoreJpaRepository.findByJti(parsedRefreshToken.jti())
                .orElseThrow(InvalidRefreshTokenException::new);

        // refreshToken 재사용 여부 판단 시 전체 폐기
        revokeFamilyIfReusedRefreshToken(storedRefreshToken);

        // 사용자 조회
        MemberAccount memberAccount = memberAccountJpaRepository.findByEmailAndDeletedAtIsNull(parsedRefreshToken.subject())
                .orElseThrow(InvalidRefreshTokenException::new);

        // 기존 refreshToken 소프트 삭제
        storedRefreshToken.softDelete();

        // 새 토큰 생성
        Instant now = Instant.now();
        String accessToken = jwtTokenProvider.createAccessToken(memberAccount, now);
        IssuedRefreshToken refreshToken = jwtTokenProvider.createRefreshToken(memberAccount, parsedRefreshToken.familyId(), now);

        // DB에 생성된 refreshToken 저장
        RefreshTokenStore refreshTokenStore = RefreshTokenStore.builder()
                .jti(refreshToken.jti())
                .expiresAt(refreshToken.expiresAt())
                .familyId(refreshToken.familyId())
                .memberAccount(memberAccount)
                .build();
        refreshTokenStoreJpaRepository.save(refreshTokenStore);

        // return
        return ReIssueResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.token())
                .build();
    }

    /**
     * refreshToken 재사용 여부 판단 시 전체 폐기
     *
     * @param storedRefreshToken 저장된 refreshToken
     */
    private void revokeFamilyIfReusedRefreshToken(RefreshTokenStore storedRefreshToken) {
        if (storedRefreshToken.isSoftDeleted()) {
            refreshTokenStoreJpaRepository.findAllByFamilyIdAndDeletedAtIsNull(storedRefreshToken.getFamilyId())
                    .forEach(RefreshTokenStore::softDelete);
            throw new InvalidRefreshTokenException();
        }
    }
}
