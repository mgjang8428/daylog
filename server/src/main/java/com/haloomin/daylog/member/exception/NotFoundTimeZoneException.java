package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class NotFoundTimeZoneException extends ServiceException {
    public NotFoundTimeZoneException() {
        super(ServiceErrorCode.E100006);
    }
}
