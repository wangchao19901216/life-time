package com.lifetime.common.dataSource.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:wangchao
 * @date: 2024/12/28-21:22
 * @description: com.lifetime.common.dataSource.model
 * @Version:1.0
 */
@Data
public class SchemaModel  implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 数据源Id */
    private String dataSourceId;
    /** 库 */
    private String catalog;
    /** 模式 */
    private String schema;

}
