package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class EmailNotFoundException extends ServiceException {
    public EmailNotFoundException() {
        super(ServiceErrorCode.E100003);
    }
}
