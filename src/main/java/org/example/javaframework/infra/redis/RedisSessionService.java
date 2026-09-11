package org.example.javaframework.infra.redis;

import org.example.javaframework.infra.SessionService;
import org.example.javaframework.infra.model.UserInfo;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Optional;

public class RedisSessionService implements SessionService {

    private static final String KEY_PREFIX = "session:";

    private final RedisTemplate<String, UserInfo> redisTemplate;

    public RedisSessionService(RedisTemplate<String, UserInfo> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String accessToken, UserInfo userInfo, Duration ttl) {
        redisTemplate.opsForValue().set(KEY_PREFIX + accessToken, userInfo, ttl);
    }

    @Override
    public Optional<UserInfo> findByAccessToken(String accessToken) {
        try {
            UserInfo session = redisTemplate.opsForValue().get(KEY_PREFIX + accessToken);
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