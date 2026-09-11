package org.example.javaframework.configuration;

import org.example.javaframework.infra.security.JwtProvider;
import org.example.javaframework.web.common.ErrorResponseWriter;
import org.example.javaframework.web.common.ErrorCode;
import org.example.javaframework.web.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Import({JwtProvider.class, ErrorResponseWriter.class})
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtProvider jwtProvider,
            ErrorResponseWriter errorResponseWriter) throws Exception {

        var jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider, errorResponseWriter);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                errorResponseWriter.write(res, ErrorCode.UNAUTHORIZED))
                        .accessDeniedHandler((req, res, e) ->
                                errorResponseWriter.write(res, ErrorCode.FORBIDDEN))
                );

        return http.build();
    }
}
