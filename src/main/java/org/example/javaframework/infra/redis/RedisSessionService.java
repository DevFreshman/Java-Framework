package org.example.javaframework.infra.redis;

import lombok.extern.slf4j.Slf4j;
import org.example.javaframework.infra.SessionService;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Optional;

@Slf4j
public class RedisSessionService implements SessionService {


    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSessionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> void save(String key, T data, Duration ttl) {
        redisTemplate.opsForValue().set(key, data, ttl);
    }

    @Override
    public <T> Optional<T> find(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (type.isInstance(value)) {
                return Optional.of(type.cast(value));
            }
        } catch (Exception e) {
            log.error("Error retrieving value from Redis for key {}", key);
            log.debug(e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Error deleting value from Redis for key {}", key);
            log.debug(e.getMessage());
        }
    }
}