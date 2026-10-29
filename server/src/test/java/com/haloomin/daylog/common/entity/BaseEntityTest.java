package com.haloomin.daylog.common.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.haloomin.daylog.common.exception.AlreadySoftDeletedException;
import com.haloomin.daylog.common.exception.NotSoftDeletedException;

class BaseEntityTest {

    static class TestEntity extends BaseEntity {
        // 테스트용 엔티티
    }

    @Test
    void 소프트삭제를_하면_삭제된_상태가_된다() {
        TestEntity testEntity = new TestEntity();

        testEntity.softDelete();

        assertTrue(testEntity.isSoftDeleted());
    }

    @Test
    void 이미_삭제된_상태에서_소프트삭제를_하면_예외가_발생한다() {
        TestEntity testEntity = new TestEntity();

        testEntity.softDelete();

        assertThrows(AlreadySoftDeletedException.class, testEntity::softDelete);
    }

    @Test
    void 소프트삭제취소를_하면_삭테처리가_취소된다() {
        TestEntity testEntity = new TestEntity();

        testEntity.softDelete();
        testEntity.cancelSoftDelete();

        assertTrue(!testEntity.isSoftDeleted());
    }

    @Test
    void 삭제되지_않은_상태에서_삭제취소를_하면_예외가_발생한다() {
        TestEntity testEntity = new TestEntity();

        // member is not softDelete

        assertThrows(NotSoftDeletedException.class, testEntity::cancelSoftDelete);
    }

    @Test
    void 소프트삭제_여부를_확인할_수_있다() {
        TestEntity testEntity = new TestEntity();

        // 소프트삭제 되지 않은 상태 확인
        assertTrue(!testEntity.isSoftDeleted());

        testEntity.softDelete();

        // 소프트삭제 된 상태 확인
        assertTrue(testEntity.isSoftDeleted());
    }
}