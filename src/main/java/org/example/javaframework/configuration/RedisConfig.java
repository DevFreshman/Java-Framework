package org.example.javaframework.configuration;

import org.example.javaframework.infra.SessionService;
import org.example.javaframework.infra.model.UserInfo;
import org.example.javaframework.infra.redis.RedisSessionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UserInfo> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserInfo> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(GenericJacksonJsonRedisSerializer.builder().build());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(GenericJacksonJsonRedisSerializer.builder().build());
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(SessionService.class)
    public SessionService sessionService(RedisTemplate<String, UserInfo> redisTemplate) {
        return new RedisSessionService(redisTemplate);
    }
}