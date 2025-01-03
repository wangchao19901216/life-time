package com.lifetime.common.dataSource.service;

import com.lifetime.common.dataSource.entity.DataSourceEntity;
import com.lifetime.common.service.BaseService;

import java.util.Map;

/**
 * @author:wangchao
 * @date: 2024/12/28-19:38
 * @description: com.lifetime.common.manager.service
 * @Version:1.0
 */
public interface IDataSourceService extends BaseService<DataSourceEntity> {

    public String testConnection(DataSourceEntity dataSource);

    public Object execute(String datasourceId, String datasourceType, String schema,String operaType, String sql, Map<String, Object> params);
}
