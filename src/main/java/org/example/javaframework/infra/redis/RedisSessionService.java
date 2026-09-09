package org.example.javaframework.infra.redis;

import org.example.javaframework.infra.SessionService;
import org.example.javaframework.infra.security.UserSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisSessionService implements SessionService {

    private static final String KEY_PREFIX = "session:";

    private final RedisTemplate<String, UserSession> redisTemplate;

    public RedisSessionService(RedisTemplate<String, UserSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String accessToken, UserSession session, Duration ttl) {
        redisTemplate.opsForValue().set(KEY_PREFIX + accessToken, session, ttl);
    }

    @Override
    public Optional<UserSession> findByAccessToken(String accessToken) {
        try {
            UserSession session = redisTemplate.opsForValue().get(KEY_PREFIX + accessToken);
            return Optional.ofNullable(session);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteByAccessToken(String accessToken) {
        try {
            redisTemplate.delete(KEY_PREFIX + accessToken);
        } catch (Exception e) {
            // Log the exception
        }
    }
}