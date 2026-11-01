package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class NotAllowedUppercaseEmailException extends ServiceException {
    /**
     * E100005
     */
    public NotAllowedUppercaseEmailException() {
        super(ServiceErrorCode.E100005);
    }
}
