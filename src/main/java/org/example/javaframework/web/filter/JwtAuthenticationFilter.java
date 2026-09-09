package org.example.javaframework.web.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.javaframework.infra.security.CurrentUserContext;
import org.example.javaframework.infra.security.JwtProvider;
import org.example.javaframework.infra.security.UserSession;
import org.example.javaframework.web.common.ErrorResponseWriter;
import org.example.javaframework.web.common.ErrorCode;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ErrorResponseWriter errorResponseWriter;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, ErrorResponseWriter errorResponseWriter) {
        this.jwtProvider = jwtProvider;
        this.errorResponseWriter = errorResponseWriter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtProvider.verifyAndParse(token);

            UserSession session = new UserSession(
                    claims.getSubject(),
                    claims.get("username", String.class),
                    claims.get("role", String.class),
                    claims.get("domain", String.class)
            );

            MDC.put("userId", session.userId());

            var authentication = new UsernamePasswordAuthenticationToken(
                    session, null, session.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CurrentUserContext.set(session);

            chain.doFilter(request, response);

        } catch (ExpiredJwtException | SignatureException | MalformedJwtException e) {
            errorResponseWriter.write(response, ErrorCode.UNAUTHORIZED);
        } finally {
            CurrentUserContext.clear();
        }
    }
}