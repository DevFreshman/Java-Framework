package org.example.javaframework.annotation;


import org.example.javaframework.configuration.SecurityConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SecurityConfig.class)
public @interface EnableFrameworkSecurity {
}
