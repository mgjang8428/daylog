package com.haloomin.daylog.common.exception.service;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class NotSoftDeletedException extends ServiceException {
    public NotSoftDeletedException() {
        super(ServiceErrorCode.E000003);
    }
}
