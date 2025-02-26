package com.lifetime.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lifetime.common.component.LtMetaObjectHandler;
import com.lifetime.common.dataSource.config.JdbcDataSourceRouter;
import com.lifetime.common.dataSource.driver.DataSourceManager;
import com.lifetime.common.dataSource.driver.jdbc.JdbcDataSourceDriver;
import com.lifetime.common.dataSource.mapper.DataHandleMapper;
import com.lifetime.common.dataSource.spi.IDataSourceDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.HashMap;

/**
 * @author:wangchao
 * @date: 2025/1/2-09:49
 * @description: com.lifetime.common.config
 * @Version:1.0
 */
@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    /**
     *  mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 默认系统数据源
     *
     * @return
     */
    @Bean
    public JdbcDataSourceRouter defaultDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(1);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(20);
        // 配置获取连接等待超时的时间
        dataSource.setMaxWait(6000);
        dataSource.setKeepAlive(true);
        //  配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setMaxEvictableIdleTimeMillis(900000);
        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setTestOnReturn(false);

        //配置缺省的数据源
        JdbcDataSourceRouter dynamic = new JdbcDataSourceRouter();
        dynamic.setTargetDataSources(new HashMap<>());
        // 设置默认数据源
        dynamic.setDefaultTargetDataSource(dataSource);
        return dynamic;
    }

    /**
     * JDBC 数据源驱动
     *
     * @param dataHandleMapper
     * @return
     */
    @Bean
    public IDataSourceDriver dataSourceDriver(DataHandleMapper dataHandleMapper) {
        return new JdbcDataSourceDriver(dataHandleMapper);
    }

    /**
     * 数据源插件驱动
     *
     * @param iDataSourceDriver
     * @return
     */
    @Bean
    public DataSourceManager dataSourceManager(IDataSourceDriver iDataSourceDriver) {
        return new DataSourceManager(iDataSourceDriver);
    }


    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        //用mybatis的这里会有点区别，mybatis用的是SqlSessionFactoryBean
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(defaultDataSource());

        // 指定Mapper文件的位置
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"));
        sqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor());

        // 构建MyBatisPlus全局配置对象，用于指定元对象字段填充
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new LtMetaObjectHandler());
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);

        MybatisConfiguration mybatisConfiguration=new MybatisConfiguration();
        mybatisConfiguration.setCallSettersOnNulls(true);
        sqlSessionFactoryBean.setConfiguration(mybatisConfiguration);

        return sqlSessionFactoryBean.getObject();
    }

}
