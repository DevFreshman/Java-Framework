package org.example.javaframework.infra;

import org.example.javaframework.infra.security.UserSession;

import java.time.Duration;
import java.util.Optional;

public interface SessionService {
    void save(String accessToken, UserSession session, Duration ttl);
    Optional<UserSession> findByAccessToken(String accessToken);
    void deleteByAccessToken(String accessToken);
}
