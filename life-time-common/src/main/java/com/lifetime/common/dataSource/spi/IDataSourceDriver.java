package com.lifetime.common.dataSource.spi;

import com.lifetime.common.dataSource.entity.DataSourceEntity;

/**
 * @author:wangchao
 * @date: 2024/12/28-21:00
 * @description: 数据源驱动
 * @Version:1.0
 */

public interface IDataSourceDriver<T> {
    /**
     * 数据源名称
     *
     * @return
     */
    String getName();


    /**
     * 测试连接
     *
     * @param dataSource
     * @return
     */
    String testConnect(DataSourceEntity dataSource);

    /**
     * 初始化
     *
     * @param dataSource
     */
    void init(DataSourceEntity dataSource);

    /**
     * 销毁
     *
     * @param dataSourceId
     */
    void destroy(String dataSourceId);

    /**
     * 元数据对象
     *
     * @return
     */
    IMetaDataMapper getMetaData();


    /**
     * 处理对象
     *
     * @return
     */
    IStatementMapper getStatement();


}
