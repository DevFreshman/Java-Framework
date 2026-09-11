package org.example.javaframework.infra.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
@EnableConfigurationProperties(JwtProperties.class)
@Validated
@Slf4j
public class JwtProvider {

    private final SecretKey signingKey;

    @Getter
    private final long expirationMs;

    public JwtProvider(JwtProperties jwtProperties) {

        if (jwtProperties.getSecret() == null || jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "framework.security.jwt.secret phải có ít nhất 32 ký tự (256 bit) cho thuật toán HS256");
        }

        String secret = jwtProperties.getSecret();
        long tll = jwtProperties.getTtlSeconds();
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        log.debug("Secret Key: {}", secret);
        log.debug("Secret key: {}", secretKey);
        log.debug("TTL: {}", tll);

        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expirationMs = jwtProperties.getTtlSeconds() * 1000;;
    }

    public String generateToken(String userId, String username, String role, String domain) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId)
                .claim("username", username)
                .claim("role", role)
                .claim("domain", domain)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public Claims verifyAndParse(String token) {
        try {
            log.debug("Verifying JWT");

            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.debug("Parsed claims: {}", claims);

            return claims;

        } catch (ExpiredJwtException e) {
            log.error("JWT expired: {}", e.getMessage());
            throw e;

        } catch (SignatureException e) {
            log.error("JWT signature invalid: {}", e.getMessage());
            throw e;

        } catch (MalformedJwtException e) {
            log.error("JWT malformed: {}", e.getMessage());
            throw e;

        } catch (UnsupportedJwtException e) {
            log.error("JWT unsupported: {}", e.getMessage());
            throw e;

        } catch (IllegalArgumentException e) {
            log.error("JWT token is null or empty: {}", e.getMessage());
            throw e;
        }
    }

}