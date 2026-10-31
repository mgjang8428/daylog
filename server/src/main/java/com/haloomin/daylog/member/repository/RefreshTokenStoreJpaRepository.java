package com.haloomin.daylog.member.repository;

import com.haloomin.daylog.member.entity.RefreshTokenStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenStoreJpaRepository extends JpaRepository<RefreshTokenStore, Long> {
    /**
     * jti로 refreshToken 찾기
     *
     * @param jti
     * @return
     */
    Optional<RefreshTokenStore> findByJti(String jti);

    /**
     * 삭제 되지 않은 familyId가 같은 refreshToken 찾기
     *
     * @param familyId
     * @return
     */
    List<RefreshTokenStore> findAllByFamilyIdAndDeletedAtIsNull(String familyId);
}
