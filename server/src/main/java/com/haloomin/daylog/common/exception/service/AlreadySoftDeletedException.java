package com.haloomin.daylog.common.exception.service;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class AlreadySoftDeletedException extends ServiceException {
    public AlreadySoftDeletedException() {
        super(ServiceErrorCode.E000002);
    }
}
