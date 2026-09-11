package org.example.javaframework.annotation;

import org.example.javaframework.configuration.RedisConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisConfig.class)
public @interface EnableFrameworkSession {
}
