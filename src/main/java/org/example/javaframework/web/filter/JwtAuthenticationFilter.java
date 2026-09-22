package org.example.javaframework.web.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.javaframework.infra.security.CurrentUserContext;
import org.example.javaframework.infra.security.JwtProvider;
import org.example.javaframework.infra.security.UserSession;
import org.example.javaframework.web.common.ErrorCode;
import org.example.javaframework.web.common.ErrorResponseWriter;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ErrorResponseWriter errorResponseWriter;

    public JwtAuthenticationFilter(
            JwtProvider jwtProvider,
            ErrorResponseWriter errorResponseWriter
    ) {
        this.jwtProvider = jwtProvider;
        this.errorResponseWriter = errorResponseWriter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            log.debug("No JWT token found in request headers");
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtProvider.verifyAndParse(token);

            UserSession session = new UserSession(
                    token,
                    claims.getSubject(),
                    claims.get("username", String.class),
                    claims.get("role", String.class),
                    claims.get("status", String.class)
            );

            // Check account status
            if (!"ACTIVE".equals(session.status())) {
                log.debug(
                        "Inactive user attempted to access resource: userId={}, status={}",
                        session.userId(),
                        session.status()
                );

                errorResponseWriter.write(
                        response,
                        ErrorCode.FORBIDDEN
                );
                return;
            }

            MDC.put("userId", session.userId());

            var authentication = new UsernamePasswordAuthenticationToken(
                    session,
                    null,
                    session.getAuthorities()
            );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            CurrentUserContext.set(session);

            log.debug(
                    "User authenticated: userId={}, username={}",
                    session.userId(),
                    session.username()
            );

            chain.doFilter(request, response);

        } catch (ExpiredJwtException |
                 SignatureException |
                 MalformedJwtException e) {

            log.debug("Invalid JWT: {}", e.getMessage());

            errorResponseWriter.write(
                    response,
                    ErrorCode.UNAUTHORIZED
            );

        } finally {
            CurrentUserContext.clear();
            MDC.remove("userId");
        }
    }
}