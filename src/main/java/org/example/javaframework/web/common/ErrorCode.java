package org.example.javaframework.web.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements InterfaceErrorCode {
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_HEADER(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;
    ErrorCode(HttpStatus httpStatus) { this.httpStatus = httpStatus; }

    @Override public String getCode() { return name(); }
    @Override public HttpStatus getHttpStatus() { return httpStatus; }
}
