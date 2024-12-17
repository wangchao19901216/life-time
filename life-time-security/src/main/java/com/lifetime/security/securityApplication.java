package com.lifetime.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@ComponentScan(basePackages = "com.lifetime.*")
@MapperScan(basePackages = {"com.lifetime.common.manager.dao","com.lifetime.common.mapper"})
@EnableEurekaClient
@EnableOpenApi
public class securityApplication
{
    public static void main(String[] args) {
        SpringApplication.run(securityApplication.class,args);
    }
}
