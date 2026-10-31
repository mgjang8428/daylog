package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class PasswordConfirmFailedException extends ServiceException {
    public PasswordConfirmFailedException() {
        super(ServiceErrorCode.E100007);
    }
}
