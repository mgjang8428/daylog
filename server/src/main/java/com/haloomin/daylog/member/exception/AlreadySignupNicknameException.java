package com.haloomin.daylog.member.exception;

import com.haloomin.daylog.common.ServiceErrorCode;
import com.haloomin.daylog.common.exception.root.ServiceException;

public class AlreadySignupNicknameException extends ServiceException {
    public AlreadySignupNicknameException() {
        super(ServiceErrorCode.E100002);
    }
}
