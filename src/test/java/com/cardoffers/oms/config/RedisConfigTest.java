package com.cardoffers.oms.config;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

class RedisConfigTest {

    private RedisConfig redisConfig;

    @Mock
    private String redisHost;

    @Mock
    private int redisPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        redisConfig = new RedisConfig();
        // Manually inject the mocked values
        redisConfig.setRedisHost("localhost");
        redisConfig.setRedisPort(6379);
    }

    @Test
    void shouldReturnLettuceConnectionFactory_whenCalled() {
        LettuceConnectionFactory connectionFactory = redisConfig.redisConnectionFactory();
        assertNotNull(connectionFactory);
        assertEquals("localhost", connectionFactory.getHostName());
        assertEquals(6379, connectionFactory.getPort());
    }

    @Test
    void shouldReturnRedisTemplate_whenCalled() {
        RedisTemplate<String, Object> template = redisConfig.redisTemplate();
        assertNotNull(template);
        assertNotNull(template.getConnectionFactory());
        assertTrue(template.getKeySerializer() instanceof StringRedisSerializer);
    }

    @Test
    void shouldReturnCacheManager_whenCalled() {
        CacheManager cacheManager = redisConfig.cacheManager();
        assertNotNull(cacheManager);
        assertNotNull(cacheManager.getCacheNames());
    }

    @Test
    void shouldReturnObjectMapper_whenCalled() {
        ObjectMapper objectMapper = redisConfig.redisObjectMapper();
        assertNotNull(objectMapper);
        assertFalse(objectMapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }
}