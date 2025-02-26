package com.lifetime.common.dataSource.driver;

import com.lifetime.common.dataSource.entity.DataSourceEntity;
import com.lifetime.common.dataSource.service.IDataSourceService;
import com.lifetime.common.dataSource.spi.IDataSourceDriver;
import com.lifetime.common.dataSource.spi.IMetaDataMapper;
import com.lifetime.common.dataSource.spi.IStatementMapper;
import com.lifetime.common.exception.CommonException;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:wangchao
 * @date: 2024/12/28-22:01
 * @description: 数据源驱动管理
 * @Version:1.0
 */

public class DataSourceManager {

    @Autowired
    IDataSourceService iDataSourceService;

    /**
     * JDBC数据源连接池
     */
    public static final Map<String, DataSource> DATA_SOURCE_POOL_JDBC = new ConcurrentHashMap<>();


    /**
     * 支持的关系型数据源类型
     */
    private static final List<String> JDBC_DATA_SOURCE_TYPE = Arrays.asList("mysql", "mariadb", "oracle", "sqlserver", "postgresql","sybase", "db2","doris", "sqlite", "tidb", "opengauss", "oceanbase", "polardb", "tdsql", "dm", "gbase", "hive2");
    /**
     * 默认数据源驱动实现
     */
    private IDataSourceDriver defaultDriver;

    public DataSourceManager(IDataSourceDriver dataSourceDriver) {
        // 默认JDBC驱动
        this.defaultDriver = dataSourceDriver;
    }


    /**
     * 创建数据源
     *
     * @param dataSource
     */
    public void createDataSource(DataSourceEntity dataSource) {
        this.defaultDriver.init(dataSource);
    }

    /**
     * 测试数据源
     *
     * @param dataSource
     * @return
     */
    public String testConnect(DataSourceEntity dataSource) {
        String datasourceType = dataSource.getDataSourceType();
        if (JDBC_DATA_SOURCE_TYPE.contains(datasourceType.toLowerCase())) {
            return defaultDriver.testConnect(dataSource);
        } else {
            throw new CommonException(500,"暂不支持" + datasourceType + "数据源类型！");
        }
    }


    /**
     * 获取数据源驱动
     *
     * @param dataSourceId
     * @return
     */
    public IDataSourceDriver getDataSource(String dataSourceId) {
        IDataSourceDriver dataSourceDriver = null;
        DataSource dataSource = DATA_SOURCE_POOL_JDBC.get(dataSourceId);
        if (dataSource != null) {
            dataSourceDriver = this.defaultDriver;
        }
        if (dataSourceDriver == null) {
            DataSourceEntity dataSourceEntity=iDataSourceService.getById(dataSourceId);
            createDataSource(dataSourceEntity);
            dataSourceDriver = this.defaultDriver;
        }
        return dataSourceDriver;
    }


    /**
     * 元数据对象
     * @param datasourceId
     * @return
     */
    public IMetaDataMapper getMetaData(String datasourceId){
        IDataSourceDriver dataSource = this.getDataSource(datasourceId);
        return dataSource.getMetaData();
    }

    /**
     * 数据处理对象
     * @param datasourceId
     * @return
     */
    public IStatementMapper getStatementMapper(String datasourceId){
        IDataSourceDriver dataSource = this.getDataSource(datasourceId);
        return dataSource.getStatement();
    }

    /**
     * 删除数据源驱动
     *
     * @param datasourceId
     */
    public void remove(String datasourceId) {
        DataSource dataSource = DATA_SOURCE_POOL_JDBC.get(datasourceId);
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            hikariDataSource.close();
        }
        DATA_SOURCE_POOL_JDBC.remove(datasourceId);
    }

}
