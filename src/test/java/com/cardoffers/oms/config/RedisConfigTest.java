package com.cardoffers.oms.config;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisConfigTest {

    @InjectMocks
    private RedisConfig redisConfig;

    @Test
void shouldRedisConnectionFactory_happyPath() {
    // Given
    RedisConfig sut = new RedisConfig();

    // When
    LettuceConnectionFactory result = sut.redisConnectionFactory();

    // Then
    assertNotNull(result);
    assertInstanceOf(LettuceConnectionFactory.class, result);
}

    @Test
void shouldRedisTemplate_happyPath() {
    // Given
    RedisConfig sut = new RedisConfig();
    
    // When
    RedisTemplate<String, Object> result = sut.redisTemplate();

    // Then
    assertNotNull(result);
    assertInstanceOf(RedisTemplate.class, result);
}

    @Test
void shouldCacheManager_happyPath() {
    // Given
    RedisConfig sut = new RedisConfig();
    when(sut.redisObjectMapper()).thenReturn(new ObjectMapper());
    
    // When
    CacheManager result = sut.cacheManager();
    
    // Then
    assertNotNull(result);
    assertInstanceOf(CacheManager.class, result);
}

    @Test
void shouldRedisObjectMapper_happyPath() {
    // Given
    RedisConfig sut = new RedisConfig();

    // When
    ObjectMapper result = sut.redisObjectMapper();

    // Then
    assertNotNull(result);
    assertInstanceOf(ObjectMapper.class, result);
}

}