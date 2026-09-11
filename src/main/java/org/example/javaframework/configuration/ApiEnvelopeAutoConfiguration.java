package org.example.javaframework.configuration;

import org.example.javaframework.web.api.ApiEnvelopeAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ApiEnvelopeAutoConfiguration {
    @Bean
    public ApiEnvelopeAdvice apiEnvelopeAdvice() {
        return new ApiEnvelopeAdvice();
    }
}