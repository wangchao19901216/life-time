package com.lifetime.common.dataSource.driver.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;
import com.lifetime.common.dataSource.config.JdbcDataSourceRouter;
import com.lifetime.common.dataSource.entity.DataSourceEntity;
import com.lifetime.common.dataSource.spi.IDataSourceDriver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author:wangchao
 * @date: 2024/12/29-10:25
 * @description: com.lifetime.common.dataSource.driver
 * @Version:1.0
 */

public abstract class DefaultDataSourceDriver implements IDataSourceDriver {

    @Override
    public String getName() {
        return "jdbc";
    }

    @Override
    public String testConnect(DataSourceEntity baseDataSource) {
        Connection connection = null;
        try {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setUsername(baseDataSource.getUserName());
            dataSource.setPassword(baseDataSource.getPassWord());
            String jdbcUrl = baseDataSource.getDataSourceUrl();
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setInitializationFailTimeout(1);
            dataSource.setConnectionTimeout(10000);
            this.setDriverClass(dataSource, baseDataSource.getDataSourceType());
            connection = dataSource.getConnection();
        } catch (Exception e) {
            Throwable cause = e.getCause();
            return cause == null ? e.getMessage() : cause.getLocalizedMessage();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return "1";
    }

    @Override
    public void init(DataSourceEntity ds) {
        String datasourceId = String.valueOf(ds.getId());
        DataSource oldDataSource = null;
        if (JdbcDataSourceRouter.exist(datasourceId)) {
            oldDataSource = JdbcDataSourceRouter.getDataSource(datasourceId);
        }


        HikariDataSource dataSource = new HikariDataSource(getHikariConfig(ds));

        JdbcDataSourceRouter.setDataSource(datasourceId, dataSource);
        // 销毁旧的数据源链接
        if (oldDataSource != null) {
            if (oldDataSource instanceof DruidDataSource) {
                DruidDataSource druidDataSource = (DruidDataSource) oldDataSource;
                druidDataSource.close();
            } else if (oldDataSource instanceof HikariDataSource) {
                HikariDataSource hikariDataSource = (HikariDataSource) oldDataSource;
                hikariDataSource.close();
            }
        }
    }

    @Override
    public void destroy(String dataSourceId) {
        JdbcDataSourceRouter.destroy(dataSourceId);
    }

    /**
     * 加载特殊驱动
     * @param dataSource
     * @param datasourceType
     */
    private void setDriverClass(HikariDataSource dataSource, String datasourceType){
        if("dm".equals(datasourceType)){
            dataSource.setDriverClassName(JdbcConstants.DM_DRIVER);
        } else if (datasourceType.startsWith("gbase8")) {
            dataSource.setDriverClassName(JdbcConstants.GBASE_DRIVER);
        } else if (datasourceType.startsWith("kingbase8")) {
            dataSource.setDriverClassName(JdbcConstants.KINGBASE8_DRIVER);
        } else if ("xugu".equals(datasourceType)) {
            dataSource.setDriverClassName(JdbcConstants.XUGU_DRIVER);
        } else if ("oceanbase".equals(datasourceType)) {
            dataSource.setDriverClassName(JdbcConstants.OCEANBASE_DRIVER2);
            // SyBase
        } else if (dataSource.getJdbcUrl().toLowerCase().startsWith("jdbc:jtds:")) {
            dataSource.setConnectionTestQuery("SELECT 1");
            dataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
        }
    }



    private  HikariConfig getHikariConfig(DataSourceEntity ds){
        //创建对象
        HikariConfig hikariConfig = new HikariConfig();
        //连接池中保留的最大连接数
        hikariConfig.setMaximumPoolSize(100);
        //设置驱动
        hikariConfig.setDriverClassName(ds.driver);
        //设置连接的url
        hikariConfig.setJdbcUrl(ds.dataSourceUrl);
        //设置数据库用户名
        hikariConfig.setUsername(ds.getUserName());
        //设置数据库密码
        hikariConfig.setPassword(ds.passWord);
        //创建初始化连接池
        //设置连接池名称
        hikariConfig.setPoolName("Hikari-" + ds.getId());
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setMaxLifetime(900000);
        hikariConfig.setIdleTimeout(300000);
        hikariConfig.setConnectionTimeout(10000);
        hikariConfig.setKeepaliveTime(300000);
        return  hikariConfig;
    }
}
