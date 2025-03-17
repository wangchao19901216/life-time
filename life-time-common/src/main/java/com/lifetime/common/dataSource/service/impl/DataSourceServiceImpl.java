package com.lifetime.common.dataSource.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.constant.DataSourceConstants;
import com.lifetime.common.dataSource.dao.DataSourceMapper;
import com.lifetime.common.dataSource.driver.DataSourceManager;
import com.lifetime.common.dataSource.spi.IStatementMapper;
import com.lifetime.common.manager.dao.RoleMapper;
import com.lifetime.common.dataSource.entity.DataSourceEntity;
import com.lifetime.common.dataSource.service.IDataSourceService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author:wangchao
 * @date: 2023/9/14-14:26
 * @description: com.general.common.base.service.impl
 * @Version:1.0
 */
@Service
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSourceEntity> implements IDataSourceService {
    @Autowired
    RoleMapper  mapper;

    @Autowired
    DataSourceManager dataSourceManager;


    @Override
    public SearchResponse<DataSourceEntity> searchList(SearchRequest searchRequest) {
        QueryModel<DataSourceEntity> myQuery = QueryUtil.buildMyQuery(searchRequest, DataSourceEntity.class);
        SearchResponse<DataSourceEntity> searchResponse = QueryUtil.executeQuery(searchRequest,myQuery, this);
        return searchResponse;
    }

    @Override
    public String testConnection(DataSourceEntity dataSource) {
        return dataSourceManager.testConnect(dataSource);
    }

    @Override
    public Object execute(String datasourceId, String datasourceType, String schema,String operateType, String sql, Map<String, Object> params) {
        IStatementMapper statementMapper = dataSourceManager.getStatementMapper(datasourceId);
        // 数据源类型
        if (datasourceType != null && !params.containsKey(DataSourceConstants.DATA_SOURCE_TYPE)) {
            params.put(DataSourceConstants.DATA_SOURCE_TYPE, datasourceType);
        }
        String sqlType = operateType;
        if ("insert".equalsIgnoreCase(sqlType)) {
            return statementMapper.insert(datasourceId, schema, sql, params);
        }else if("update".equalsIgnoreCase(sqlType)){
            return statementMapper.update(datasourceId, schema, sql, params);
        }else if("delete".equalsIgnoreCase(sqlType)){
            return statementMapper.delete(datasourceId, schema, sql, params);
        } else {
            String pageSetUp=String.valueOf(params.get(DataSourceConstants.PAGE_SETUP));
            Object pageIndex = params.get(DataSourceConstants.PAGE_INDEX);
            Object pageSize = params.get(DataSourceConstants.PAGE_SIZE);
            if (pageSetUp.equals("1") && pageIndex != null && pageSize != null) {
                return statementMapper.selectPage(datasourceId, schema, sql, params, Integer.parseInt(pageIndex.toString()), Integer.parseInt(pageSize.toString()));
            } else {
                return statementMapper.selectList(datasourceId, schema, sql, params);
            }
        }
    }

}
