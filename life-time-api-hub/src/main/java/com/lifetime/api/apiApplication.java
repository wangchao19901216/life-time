package com.lifetime.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author:wangchao
 * @date: 2025/2/20-14:37
 * @description: com.lifetime.api
 * @Version:1.0
 */

@SpringBootApplication()
@EnableOpenApi
@EnableEurekaClient
@ComponentScan(basePackages = "com.lifetime.*")
@MapperScan(basePackages = {
        "com.lifetime.common.mapper",
        "com.lifetime.common.manager.dao",
        "com.lifetime.common.dataSource.dao",
        "com.lifetime.common.dataSource.mapper",
        "com.lifetime.api.dao"
})
public class apiApplication {
    public static void main( String[] args )
    {
        SpringApplication.run(apiApplication.class,args);
    }
}
