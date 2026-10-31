package com.haloomin.daylog.api.v1.common.exceptionadvice;

import com.haloomin.daylog.api.v1.common.dto.ResponseV1;
import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;
import com.haloomin.daylog.common.exception.service.AlreadySoftDeletedException;
import com.haloomin.daylog.common.exception.service.NotSoftDeletedException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@SuppressWarnings("unused")
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"com.haloomin.daylog.api.v1"})
public class CommonControllerExceptionAdvice {

    // Exception 공통 처리
    @ExceptionHandler(Exception.class)
    ResponseEntity<ResponseV1<Void>> unknownExceptionHandler(Exception e) throws Exception {
        // Security Exception은 Security가 처리하도록 적용
        if (e instanceof AccessDeniedException) {
            throw e;
        }
        if (e instanceof AuthenticationException) {
            throw e;
        }

        // Spring MVC 표준 예외 처리 405, 415
        if (e instanceof ErrorResponse errorResponse) {
            return ResponseEntity
                    .status(errorResponse.getStatusCode())
                    .body(ResponseV1.failed(ServiceErrorCode.E000000));
        }

        // 정의되지 않은 예외 처리
        log.error("정의되지 않은 오류 발생", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseV1.failed(ServiceErrorCode.E000000));
    }

    // ServiceException 공통 처리
    @ExceptionHandler(ServiceException.class)
    ResponseEntity<ResponseV1<Void>> serviceExceptionHandler(ServiceException exception) {
        return makeBadRequestResponse(exception.getServiceErrorCode());
    }

    // Validate Error 처리
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    ResponseEntity<ResponseV1<Void>> validationFailedExceptionHandler() {
        return makeBadRequestResponse(ServiceErrorCode.E000001);
    }

    @ExceptionHandler(AlreadySoftDeletedException.class)
    ResponseEntity<ResponseV1<Void>> alreadySoftDeletedExceptionHandler() {
        return makeBadRequestResponse(ServiceErrorCode.E000002);
    }

    @ExceptionHandler(NotSoftDeletedException.class)
    ResponseEntity<ResponseV1<Void>> notSoftDeletedExceptionHandler() {
        return makeBadRequestResponse(ServiceErrorCode.E000003);
    }

    private ResponseEntity<ResponseV1<Void>> makeBadRequestResponse(ServiceErrorCode serviceErrorCode) {
        return ResponseEntity.badRequest().body(ResponseV1.failed(serviceErrorCode));
    }
}
