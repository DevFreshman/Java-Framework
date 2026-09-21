package org.example.javaframework.infra;

import java.time.Duration;
import java.util.Optional;

public interface SessionService {

    <T> void save(String key, T data, Duration ttl);

    <T> Optional<T> find(String key, Class<T> type);

    void delete(String key);
}