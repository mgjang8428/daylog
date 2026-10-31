package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class NotAllowedUppercaseEmailException extends ServiceException {
    public NotAllowedUppercaseEmailException() {
        super(ServiceErrorCode.E100005);
    }
}
