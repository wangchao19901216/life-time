package com.lifetime.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author:wangchao
 * @date: 2023/5/9-15:33
 * @description: com.sh3h.common.config
 * @Version:1.0
 */
@Configuration
public class MybatisPlusConfig {
    /**
     *  mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new  PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
