package com.vhre.transactionlimitsengine.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

/**
 * Spring Cache backed by Redis (distributed cache).
 *
 * <p>Keys are plain strings; values are JSON with type information so cached
 * objects can be deserialized back to their original class.</p>
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {

    /** Default TTL applied to every cache entry. */
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(DEFAULT_TTL)
                .disableCachingNullValues()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(jsonValueSerializer()));
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          RedisCacheConfiguration redisCacheConfiguration) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }

    /**
     * JSON value serializer that keeps type information (default typing) so
     * cached objects can be read back as their original class.
     */
    private GenericJacksonJsonRedisSerializer jsonValueSerializer() {
        return GenericJacksonJsonRedisSerializer.builder()
                .enableDefaultTyping(BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build())
                .build();
    }
}
