package com.lifetime.common.dataSource.spi;

import com.lifetime.common.dataSource.model.ColumnModel;
import com.lifetime.common.dataSource.model.SchemaModel;
import com.lifetime.common.dataSource.model.TableModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/28-21:13
 * @description: com.lifetime.common.dataSource.spi
 * @Version:1.0
 */

public interface IMetaDataMapper<T>  extends LtMapper<T>{

    /**
     * 获取数据源的Catalog
     *
     * @param dataSourceId
     * @return
     */
    List<Object> getCatalogs(String dataSourceId);

    /**
     * 获取数据源Schema
     *
     * @param dataSourceId
     * @param catalog
     * @return
     */
    List<SchemaModel> getSchemas(String dataSourceId, String catalog);


    /**
     * 获取数据表列表
     *
     * @param dataSourceId
     * @param catalog
     * @param schema
     * @return
     */
    List<TableModel> getTables(String dataSourceId, String catalog, String schema);

    /**
     * 获取数据表字段列表
     *
     * @param dataSourceId
     * @param catalog
     * @param schema
     * @param table
     * @return
     */
    List<ColumnModel> getColumns(String dataSourceId, String catalog, String schema, String table);
}
