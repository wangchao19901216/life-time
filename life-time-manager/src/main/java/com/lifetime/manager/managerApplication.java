package com.lifetime.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableOpenApi
@EnableEurekaClient
@ComponentScan(basePackages = "com.lifetime.*")
@MapperScan(basePackages = {"com.lifetime.common.manager.dao",
        "com.lifetime.common.mapper",
        "com.lifetime.common.dataSource.driver",
        "com.lifetime.common.dataSource.driver.jdbc",
        "com.lifetime.common.dataSource.dao",
        "com.lifetime.common.dataSource.mapper"})
public class managerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(managerApplication.class,args);
    }
}
