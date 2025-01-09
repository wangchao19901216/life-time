package com.lifetime.manager.business;

import cn.hutool.db.meta.Column;
import com.lifetime.common.constant.DataSourceConstants;
import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.dataSource.driver.DataSourceManager;
import com.lifetime.common.dataSource.spi.IMetaDataMapper;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.dataSource.entity.DataSourceEntity;
import com.lifetime.common.dataSource.service.IDataSourceService;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/28-19:42
 * @description: com.lifetime.manager.business
 * @Version:1.0
 */
@Component
public class DataSourceBusiness {

    @Autowired
    IDataSourceService iLtDataSourceService;

    @Autowired
    private DataSourceManager dataSourceManager;


    public ResponseResult save(DataSourceEntity entity) {
        iLtDataSourceService.save(entity);
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }

    public ResponseResult remove(BigInteger id) {
        DataSourceEntity resultEntity = iLtDataSourceService.getById(id);
        if (resultEntity.getDataType() == StatusConstants.CAN_NOT_DELETE) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED_DEFAULT);
        }
        iLtDataSourceService.removeById(id);
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
//        if (departmentEntityList.size() > 0)
//            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "存在子部门，无法删除!");
//        else {
//
//        }
    }

    public ResponseResult update(BigInteger id, DataSourceEntity entity) {
        DataSourceEntity resultEntity = iLtDataSourceService.getById(id);
        if (LtCommonUtil.isBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, "数据源不存在");
        } else {
            entity.setId(resultEntity.getId());
            iLtDataSourceService.updateById(entity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult search(SearchRequest searchRequest) {
        return ResponseResult.success(iLtDataSourceService.searchList(searchRequest));
    }

    public ResponseResult getCatalogs(String datasourceId) {
        IMetaDataMapper metaData = dataSourceManager.getMetaData(datasourceId);
        List schemas= metaData.getCatalogs(datasourceId);
        return ResponseResult.success(schemas);
    }

    public ResponseResult getSchemas(String datasourceId,String catalog) {
        DataSourceEntity dataSourceEntity=iLtDataSourceService.getById(datasourceId);
        IMetaDataMapper metaData = dataSourceManager.getMetaData(datasourceId);
        List schemas;
        if (DataSourceConstants.CATALOG_DATA_SOURCE.contains(dataSourceEntity.getDataSourceType().toLowerCase())){
            schemas = metaData.getCatalogs(datasourceId);
        }else{
            schemas = metaData.getSchemas(datasourceId, catalog);
        }
        return ResponseResult.success(schemas);
    }


    public ResponseResult getTable(String datasourceId,String schema) {
        DataSourceEntity dataSourceEntity=iLtDataSourceService.getById(datasourceId);
        IMetaDataMapper metaData = dataSourceManager.getMetaData(datasourceId);
        List schemas;
        if (DataSourceConstants.CATALOG_DATA_SOURCE.contains(dataSourceEntity.getDataSourceType().toLowerCase())){
            schemas = metaData.getTables(datasourceId,schema,null);
        }else{
            schemas = metaData.getTables(datasourceId,null, schema);
        }
        return ResponseResult.success(schemas);
    }

    public ResponseResult getColumns(String datasourceId,String schema,String table) {
        DataSourceEntity dataSourceEntity=iLtDataSourceService.getById(datasourceId);
        IMetaDataMapper metaData = dataSourceManager.getMetaData(datasourceId);

        List<Column> columns;
        if (DataSourceConstants.CATALOG_DATA_SOURCE.contains(dataSourceEntity.getDataSourceType().toLowerCase())){
            columns = metaData.getColumns(datasourceId, schema, null, table);
        }else{
            columns = metaData.getColumns(datasourceId, null, schema, table);
        }
        return ResponseResult.success(columns);
    }

    public ResponseResult testConnect(BigInteger id) {
        DataSourceEntity resultEntity = iLtDataSourceService.getById(id);
        return  ResponseResult.success(iLtDataSourceService.testConnection(resultEntity));
    }
    public ResponseResult testConnect(DataSourceEntity dataSource) {
        if(LtCommonUtil.isBlankOrNull(dataSource.getId())){
            SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker(0,0);
            dataSource.setId(BigInteger.valueOf(snowflakeIdWorker.nextId()));
        }
        return  ResponseResult.success(iLtDataSourceService.testConnection(dataSource));
    }


}
