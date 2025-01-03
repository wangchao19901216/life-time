package com.lifetime.common.dataSource.model;

import lombok.Data;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:wangchao
 * @date: 2024/12/28-21:27
 * @description: com.lifetime.common.dataSource.model
 * @Version:1.0
 */

@Data
public class TableModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 数据源Id
     */
    private String dataSourceId;
    /**
     * 库
     */
    private String catalog;
    /**
     * 模式
     */
    private String schema;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表类型。典型的类型是“TABLE”，“VIEW”，“SYSTEM TABLE”，“GLOBAL TEMPORARY”，“LOCAL TEMPORARY”，“ALIAS”，“SYNONYM”。
     */
    private String tableType;
    /**
     * 表备注
     */
    private String remarks;
    /**
     * 表状态
     */
    private String status;
    /**
     * 表大小（字节单位）
     */
    private Long size;
    /**
     * 表行数
     */
    private Long rows;
    /**
     * 表创建时间
     */
    private Date createTime;
    /**
     * 表修改时间
     */
    private Date updateTime;
}
