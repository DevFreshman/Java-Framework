package org.example.javaframework.web.exception;

import org.example.javaframework.web.api.Response;
import org.example.javaframework.web.api.Errors;
import org.example.javaframework.web.common.ErrorCode;
import org.example.javaframework.web.common.InterfaceErrorCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<?>> handleBusiness(BusinessException ex) {
        InterfaceErrorCode code = ex.getErrorCode();
        String message = messageSource.getMessage(
                code.getCode(), ex.getArgs(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(code.getHttpStatus())
                .body(Response.failure(new Errors(code.getCode(), message)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<?>> handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(new Errors(ErrorCode.VALIDATION_FAILED.getCode(), detail)));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response<?>> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = "Missing required parameter: " + ex.getParameterName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(new Errors(ErrorCode.VALIDATION_FAILED.getCode(), message)));
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Parameter '%s' must be of type %s",
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(new Errors(ErrorCode.VALIDATION_FAILED.getCode(), message)));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response<?>> handleNotFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Response.failure(new Errors(ErrorCode.NOT_FOUND.getCode(), "Resource not found")));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleUnknown(Exception ex) {
        String message = messageSource.getMessage(
                ErrorCode.INTERNAL_ERROR.getCode(), null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.failure(new Errors(ErrorCode.INTERNAL_ERROR.getCode(), message)));
    }
}