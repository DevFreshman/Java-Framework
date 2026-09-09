package org.example.javaframework.configuration;

import org.example.javaframework.web.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({ GlobalExceptionHandler.class,
        WebMvcConfig.class,
        MessageConfig.class })
public class ErrorHandlingAutoConfiguration { }
