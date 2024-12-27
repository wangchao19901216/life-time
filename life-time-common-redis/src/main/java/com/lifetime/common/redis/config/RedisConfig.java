package com.lifetime.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author:wangchao
 * @date: 2023/5/16-12:54
 * @description: com.sh3h.common.taskid.config
 * @Version:1.0
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfig {

    private final StringRedisSerializer srs = new StringRedisSerializer();
    private final Jackson2JsonRedisSerializer<Object> jsonRedisSerializer =
            new Jackson2JsonRedisSerializer<>(Object.class);

    /**
     * 配置key,value的序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//
////        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer =
////                new Jackson2JsonRedisSerializer<>(Object.class);
////        ObjectMapper objectMapper=new ObjectMapper();
////        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
////        jsonRedisSerializer.setObjectMapper(objectMapper);
//
//
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(srs);
//        template.setValueSerializer(jsonRedisSerializer);
//        template.setHashKeySerializer(srs);
//        template.setHashValueSerializer(jsonRedisSerializer);
//        template.setDefaultSerializer(jsonRedisSerializer);
//        return template;

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        serializer.setObjectMapper(objectMapper);
        template.setKeySerializer(srs);
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(srs);
        template.setHashValueSerializer(serializer);
        template.setDefaultSerializer(serializer);

        return template;

    }

    /**
     * 配置使用注解的时候缓存配置，默认是序列化反序列化的形式，加上此配置则为 json 形式
     */
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory factory) {
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext
//                        .SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair
//                                .fromSerializer(new GenericJackson2JsonRedisSerializer()));
//        return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build();
//    }
}