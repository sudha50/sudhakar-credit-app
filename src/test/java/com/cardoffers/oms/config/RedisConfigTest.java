package com.cardoffers.oms.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;

class RedisConfigTest {

    @InjectMocks
    private RedisConfig redisConfig;

    @Mock
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Value("${spring.redis.host}")
    private String redisHost = "localhost";

    @Value("${spring.redis.port}")
    private int redisPort = 6379;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateLettuceConnectionFactory_whenCalled() {
        LettuceConnectionFactory factory = redisConfig.redisConnectionFactory();
        assertNotNull(factory);
        assertEquals(redisHost, factory.getHostName());
        assertEquals(redisPort, factory.getPort());
    }

    @Test
    void shouldCreateRedisTemplate_whenCalled() {
        RedisTemplate<String, Object> template = redisConfig.redisTemplate();
        assertNotNull(template);
        assertNotNull(template.getConnectionFactory());
    }

    @Test
    void shouldCreateCacheManager_whenCalled() {
        CacheManager cacheManager = redisConfig.cacheManager();
        assertNotNull(cacheManager);
        assertTrue(cacheManager.getCacheNames().isEmpty());
    }

    @Test
    void shouldCreateObjectMapper_withCorrectConfiguration() {
        ObjectMapper mapper = redisConfig.redisObjectMapper();
        assertNotNull(mapper);
        assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    @Test
    void shouldConfigureCacheManagerWithDefaultTtl() {
        CacheManager cacheManager = redisConfig.cacheManager();
        assertNotNull(cacheManager);
        Duration ttl = ((RedisCacheConfiguration) cacheManager.getCacheNames()).getTtl();
        assertEquals(Duration.ofMinutes(10), ttl);
    }
}