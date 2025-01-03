package com.lifetime.common.dataSource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.lifetime.common.constant.DataSourceConstants;
import com.lifetime.common.dataSource.driver.DataSourceManager;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.exception.CommonException;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author:wangchao
 * @date: 2024/12/28-22:13
 * @description: JDBC数据源 动态路由
 * @Version:1.0
 */
public class JdbcDataSourceRouter extends AbstractRoutingDataSource {

    /**
     * 当前线程数据源KEY
     */
    private static final ThreadLocal<String> DATA_SOURCE_KEY = new ThreadLocal<>();

    /**
     * 获取数据源key
     *
     * @return
     */
    public static String getDataSourceKey() {
        return JdbcDataSourceRouter.DATA_SOURCE_KEY.get();
    }

    /**
     * 设置数据源key
     *
     * @param key
     */
    public static void setDataSourceKey(String key) {
        JdbcDataSourceRouter.DATA_SOURCE_KEY.set(key);
    }

    /**
     * 移除数据源
     */
    public static void remove() {
        JdbcDataSourceRouter.DATA_SOURCE_KEY.remove();
    }

    /**
     * 判断数据源是否存在
     */
    public static boolean exist(String dataSourceId) {
        DataSource dataSource = DataSourceManager.DATA_SOURCE_POOL_JDBC.get(getDataSourceId(dataSourceId));
        if (dataSource != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取数据源ID
     *
     * @param dataSourceId
     * @return
     */
    private static String getDataSourceId(String dataSourceId) {
        return dataSourceId == null ? null : dataSourceId.split(":")[0];
    }


    /**
     * 销毁
     *
     * @param dataSourceId
     * @return
     */
    public static void destroy(String dataSourceId) {
        DataSource dataSource = DataSourceManager.DATA_SOURCE_POOL_JDBC.get(getDataSourceId(dataSourceId));
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            druidDataSource.close();
        } else if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            hikariDataSource.close();
        }
        DataSourceManager.DATA_SOURCE_POOL_JDBC.remove(dataSourceId);
    }

    /**
     * 获取数据源
     *
     * @param dataSourceId
     * @return
     */
    public static DataSource getDataSource(String dataSourceId) {
        DataSource dataSource = DataSourceManager.DATA_SOURCE_POOL_JDBC.get(getDataSourceId(dataSourceId));
        if (dataSource == null) {
            throw new CommonException(CommonExceptionEnum.DATA_NOT_EXIST);
        }
        return dataSource;
    }


    /**
     * 获取数据源
     *
     * @return
     */
    public static DataSource getDataSource() {
        String dataSourceKey = getDataSourceKey();
        DataSource dataSource = DataSourceManager.DATA_SOURCE_POOL_JDBC.get(getDataSourceId(dataSourceKey));
        if (dataSource == null) {
            throw new CommonException(CommonExceptionEnum.DATA_NOT_EXIST);
        }

        return dataSource;
    }

    /**
     * 添加数据源
     *
     * @param dataSourceId
     **/
    public static void setDataSource(String dataSourceId, DataSource dataSource) {
        DataSourceManager.DATA_SOURCE_POOL_JDBC.put(dataSourceId, dataSource);
    }

    /**
     * 切换数据源
     *
     * @return
     */
    @Override
    protected DataSource determineTargetDataSource() {
        Object dataSourceKey = this.determineCurrentLookupKey();
        // 默认系统数据源
        if (dataSourceKey == null) {
            return super.getResolvedDefaultDataSource();
        }
        String dataSourceId = getDataSourceId(dataSourceKey.toString());
        DataSource dataSource = DataSourceManager.DATA_SOURCE_POOL_JDBC.get(dataSourceId);

        if (dataSource == null) {
            throw new CommonException(CommonExceptionEnum.DATA_NOT_EXIST);
        }
        return dataSource;
    }

    /**
     * 获取连接
     *
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        Object dataSourceKey = null;
        try {
            connection = this.determineTargetDataSource().getConnection();
            dataSourceKey = this.determineCurrentLookupKey();
            // dataSouceId:dataSourceType:schemaName
            if (dataSourceKey != null && dataSourceKey.toString().contains(":")) {
                String[] dataSourceStr = dataSourceKey.toString().split(":");
                if (dataSourceStr.length == 3) {
                    String dataSourceType = dataSourceStr[1];
                    String schema = dataSourceStr[2];
                    if (DataSourceConstants.CATALOG_DATA_SOURCE.contains(dataSourceType)) {
                        connection.setCatalog(schema);
                    } else {
                        connection.setSchema(schema);
                    }
                }
            }
        } catch (Exception e) {
            String message = "-数据源连接获取失败,dataSourceKey:" + dataSourceKey + e;
            System.out.println(message);
        }
        return connection;
    }


    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSourceKey();
    }
}
