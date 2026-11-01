package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class InvalidRefreshTokenException extends ServiceException {
    /**
     * E100004
     */
    public InvalidRefreshTokenException() {
        super(ServiceErrorCode.E100004);
    }
}
