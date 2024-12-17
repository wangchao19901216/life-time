package com.lifetime.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2023/5/7-22:31
 * @description: com.sh3h.common.config
 * @Version:1.0
 */

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        MyMappingJackson2HttpMessageConverter messageConverter = new MyMappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = messageConverter.getObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
        messageConverter.setObjectMapper(objectMapper);
        restTemplate.getMessageConverters().add(messageConverter);
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(500000);//单位为ms
        factory.setConnectTimeout(500000);//单位为ms
        return factory;
    }

    public static class MyMappingJackson2HttpMessageConverter extends MappingJackson2CborHttpMessageConverter {
        public MyMappingJackson2HttpMessageConverter() {
            List<MediaType> mediaTypeList = new ArrayList<>();
            mediaTypeList.add(MediaType.TEXT_PLAIN);
            mediaTypeList.add(new MediaType("text", "json", StandardCharsets.UTF_8));
            setSupportedMediaTypes(mediaTypeList);
        }
    }
}
