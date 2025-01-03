package com.lifetime.common.dataSource.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:wangchao
 * @date: 2024/12/28-21:38
 * @description: com.lifetime.common.dataSource.model
 * @Version:1.0
 */
@Data
public class ColumnModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 数据源Id */
    private String datasourceId;
    /** 库 */
    private String catalog;
    /** 模式 */
    private String schema;
    /** 表名称 */
    private String tableName;

    private String columnName;

    private String remarks;
    /** 类型 */
    private String columnType;
    /** 长度 */
    private String columnSize;
    /** 精度 */
    private String decimalDigits;
    private String columnDefault;

    private String typeIcon;
}
