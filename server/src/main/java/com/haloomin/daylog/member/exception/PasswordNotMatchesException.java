package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class PasswordNotMatchesException extends ServiceException {
    public PasswordNotMatchesException() {
        super(ServiceErrorCode.E100003);
    }
}
