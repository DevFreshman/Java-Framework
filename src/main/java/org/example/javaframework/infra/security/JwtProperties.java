package org.example.javaframework.infra.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "framework.infra.security.jwt")
@Validated
@Getter
@Setter
public class JwtProperties {

    @NotBlank(message = "framework.security.jwt.secret is required")
    private String secret;

    private long ttlSeconds = 3600;
}