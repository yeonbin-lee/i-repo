package com.example.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableCaching
@EnableRedisRepositories(basePackages = "com.example") // Redis 레포지토리 기능 활성화
public class RedisConfig {
    private final RedisProperties redisProperties; // Redis 속성 정보 주입

    @Bean // 스프링 컨텍스트에 RedisConnectionFactory 빈 등록
    public RedisConnectionFactory redisConnectionFactory(){
        // LettuceConnectionFactory를 사용하여 Redis 연결 팩토리 생성, 호스트와 포트 정보를 사용
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>(); // RedisTemplate 인스턴스 생성
//        redisTemplate.setConnectionFactory(redisConnectionFactory()); // Redis 연결 팩토리 설정
//        redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키를 문자열로 직렬화하도록 설정
//        redisTemplate.setValueSerializer(new StringRedisSerializer()); // 값을 문자열로 직렬화하도록 설정
//
//        return redisTemplate; // 설정이 완료된 RedisTemplate 인스턴스를 반환
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 키와 값의 직렬화 방식 설정
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }


    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(8)) // 캐시 만료 시간 설정
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }


}