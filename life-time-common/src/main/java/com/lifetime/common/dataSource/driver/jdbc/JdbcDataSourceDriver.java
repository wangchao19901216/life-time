package com.lifetime.common.dataSource.driver.jdbc;

import com.lifetime.common.dataSource.mapper.DataHandleMapper;
import org.springframework.stereotype.Component;

/**
 * @author:wangchao
 * @date: 2025/1/2-09:51
 * @description: com.lifetime.common.dataSource.driver.jdbc
 * @Version:1.0
 */

public class JdbcDataSourceDriver extends DefaultDataSourceDriver {
    private DataHandleMapper baseDataHandleMapper;
    private JdbcStatementMapper jdbcStatement;
    private JdbcMetaDataMapper jdbcMetaData;

    public JdbcDataSourceDriver(DataHandleMapper baseDataHandleMapper) {
        this.baseDataHandleMapper = baseDataHandleMapper;
        jdbcStatement = new JdbcStatementMapper(baseDataHandleMapper);
        jdbcMetaData = new JdbcMetaDataMapper();
    }

    @Override
    public JdbcMetaDataMapper getMetaData() {
        return jdbcMetaData;
    }

    @Override
    public JdbcStatementMapper getStatement() {
        return jdbcStatement;
    }
}
