package com.cardoffers.oms.config;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.config.RedisConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RedisConfigTest {

    @Test
void shouldRedisConnectionFactory_happyPath() {
    // Given
    RedisConfig redisConfig = new RedisConfig();
    
    // When
    LettuceConnectionFactory result = redisConfig.redisConnectionFactory();
    
    // Then
    assertNotNull(result);
    // Assuming default configurations for LettuceConnectionFactory, you can assert on fields as needed
    assertEquals("localhost", result.getHostName());
    assertEquals(6379, result.getPort());
}

    @Test
void shouldRedisTemplate_happyPath() {
    // Given
    RedisTemplate<String, Object> expectedTemplate = new RedisTemplate<>();
    // Configure the expectedTemplate as necessary here

    // When
    RedisTemplate<String, Object> result = redisConfig.redisTemplate();

    // Then
    assertNotNull(result);
    assertEquals(expectedTemplate.getKeySerializer(), result.getKeySerializer());
    assertEquals(expectedTemplate.getValueSerializer(), result.getValueSerializer());
}

    @Test
void shouldCacheManager_happyPath() {
    // Given
    @Mock
    private RedisConnectionFactory redisConnectionFactory;
    @Mock
    private RedisObjectMapper redisObjectMapper;
    @InjectMocks
    private CacheManager expectedCacheManager;
    
    when(redisConnectionFactory.getConnection()).thenReturn(mock(RedisConnection.class));
    when(redisObjectMapper.create()).thenReturn(expectedCacheManager);
    
    RedisConfig redisConfig = new RedisConfig();
    ReflectionTestUtils.setField(redisConfig, "redisConnectionFactory", redisConnectionFactory);
    ReflectionTestUtils.setField(redisConfig, "redisObjectMapper", redisObjectMapper);

    // When
    CacheManager result = redisConfig.cacheManager();

    // Then
    assertNotNull(result);
    assertEquals(expectedCacheManager.getClass(), result.getClass());
}

    @Test
void shouldRedisObjectMapper_happyPath() {
    // Given
    RedisConfig redisConfig = new RedisConfig();

    // When
    ObjectMapper result = redisConfig.redisObjectMapper();

    // Then
    assertNotNull(result);
    assertEquals(JsonNode.class, result.readValue("{}", JsonNode.class).getClass());
}

}