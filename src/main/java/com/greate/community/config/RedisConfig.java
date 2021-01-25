package com.greate.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 配置类
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置 key 的序列化的方式
        template.setKeySerializer(RedisSerializer.string());
        // 设置 value 的序列化的方式
        template.setValueSerializer(RedisSerializer.json());
        // 设置 hash 的 key 的序列化的方式
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置 hash 的 value 的序列化的方式
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();

        return template;
    }

}
