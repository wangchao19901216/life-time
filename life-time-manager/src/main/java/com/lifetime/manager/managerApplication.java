package com.lifetime.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;


@SpringBootApplication
@EnableOpenApi
@EnableEurekaClient
@ComponentScan(basePackages = "com.lifetime.*")
@MapperScan(basePackages = {"com.lifetime.common.manager.dao","com.lifetime.common.mapper"})
public class managerApplication
{
    public static void main( String[] args )
    {


        SpringApplication.run(managerApplication.class,args);
    }
}
