package org.example.javaframework.web.exception;

import lombok.Getter;
import org.example.javaframework.web.common.InterfaceErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final InterfaceErrorCode ErrorCode;
    private final transient Object[] args;

    public BusinessException(InterfaceErrorCode ErrorCode, Object... args) {
        super(ErrorCode.getCode());
        this.ErrorCode = ErrorCode;
        this.args = args;
    }
}