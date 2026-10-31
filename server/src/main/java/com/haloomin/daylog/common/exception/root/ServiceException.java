package com.haloomin.daylog.common.exception.root;

import com.haloomin.daylog.common.ServiceErrorCode;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final ServiceErrorCode serviceErrorCode;

    public ServiceException(ServiceErrorCode serviceErrorCode) {
        super(serviceErrorCode.getMessage());
        this.serviceErrorCode = serviceErrorCode;
    }
}
