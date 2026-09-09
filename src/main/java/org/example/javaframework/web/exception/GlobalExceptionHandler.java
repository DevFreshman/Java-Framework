package org.example.javaframework.web.exception;


import org.example.javaframework.web.api.Response;
import org.example.javaframework.web.api.Errors;
import org.example.javaframework.web.common.ErrorCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<?>> handleBusiness(BusinessException ex) {
        ErrorCode code = ex.getInterfaceErrorCode();
        String message = messageSource.getMessage(
                code.getCode(), ex.getArgs(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(code.getHttpStatus())
                .body(Response.failure( new Errors(code.getCode(), message)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleUnknown(Exception ex) {
        String message = messageSource.getMessage(
                ErrorCode.INTERNAL_ERROR.getCode(), null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.failure( new Errors(ErrorCode.INTERNAL_ERROR.getCode(), message)));
    }
}
