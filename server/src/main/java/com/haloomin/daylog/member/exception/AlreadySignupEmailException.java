package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class AlreadySignupEmailException extends ServiceException {
    /**
     * E100001
     */
    public AlreadySignupEmailException() {
        super(ServiceErrorCode.E100001);
    }
}
