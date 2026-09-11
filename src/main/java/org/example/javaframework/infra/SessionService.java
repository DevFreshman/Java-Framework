package org.example.javaframework.infra;

import org.example.javaframework.infra.model.UserInfo;

import java.time.Duration;
import java.util.Optional;

public interface SessionService {
    void save(String accessToken, UserInfo session, Duration ttl);
    Optional<UserInfo> findByAccessToken(String accessToken);
    void deleteByAccessToken(String accessToken);
}
