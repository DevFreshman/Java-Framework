package org.example.javaframework.web.common;

import org.springframework.http.HttpStatus;

public interface InterfaceErrorCode {
    String getCode();
    HttpStatus getHttpStatus();
}
