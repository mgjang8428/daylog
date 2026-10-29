package com.haloomin.daylog.common.entity;

import com.haloomin.daylog.common.exception.AlreadySoftDeletedException;
import com.haloomin.daylog.common.exception.NotSoftDeletedException;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    private Instant deletedAt;

    /**
     * 소프트 삭제
     */
    public void softDelete() {
        // 이미 삭제된 경우 예외 처리
        if (isSoftDeleted()) {
            throw new AlreadySoftDeletedException();
        }
        // 삭제 처리
        this.deletedAt = Instant.now();
    }

    /**
     * 소프트 삭제 취소
     */
    public void cancelSoftDelete() {
        // 삭제되지 않은 경우 예외 처리
        if (!isSoftDeleted()) {
            throw new NotSoftDeletedException();
        }
        // 삭제 취소 처리
        this.deletedAt = null;
    }

    /**
     * 삭제 여부 확인
     */
    public boolean isSoftDeleted() {
        // deletedAt이 null이 아니면 삭제된 것으로 간주
        return this.deletedAt != null;
    }
}
