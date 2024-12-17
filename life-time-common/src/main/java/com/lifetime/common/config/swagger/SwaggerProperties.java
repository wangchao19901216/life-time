package com.lifetime.common.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author:wangchao
 * @date: 2023/5/7-23:15
 * @description: com.lifetime.common.swagger.config
 * @Version:1.0
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {
    /**
     * 是否开启Swagger。
     */
    private Boolean enabled=false;

    /**
     * 是否开启Swagger。
     */
    private Boolean isAuthorization;

    /**
     * Swagger解析的基础包路径。
     **/
    private String basePackage = "";

    /**
     * Swagger解析的服务包路径。
     **/
    private String serviceBasePackage = "";

    /**
     * ApiInfo中的标题。
     **/
    private String title = "";

    /**
     * ApiInfo中的描述信息。
     **/
    private String description = "";

    /**
     * ApiInfo中的版本信息。
     **/
    private String version = "";
}
