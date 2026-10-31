package com.haloomin.daylog.api.v1.common.dto;

import com.haloomin.daylog.common.ServiceErrorCode;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ResponseV1<T>(
        boolean isSuccess,
        Instant timestamp,
        T result,
        String errorCode,
        String errorMessage
) {

    public static <T> ResponseV1<T> success() {
        return ResponseV1.<T>builder()
                .isSuccess(true)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ResponseV1<T> success(T result) {
        return ResponseV1.<T>builder()
                .isSuccess(true)
                .timestamp(Instant.now())
                .result(result)
                .build();
    }

    public static <T> ResponseV1<T> failed(ServiceErrorCode serviceErrorCode) {
        return ResponseV1.<T>builder()
                .isSuccess(false)
                .timestamp(Instant.now())
                .errorCode(serviceErrorCode.name())
                .errorMessage(serviceErrorCode.getMessage())
                .build();
    }
}
