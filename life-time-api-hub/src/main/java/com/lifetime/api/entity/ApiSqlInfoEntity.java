package com.lifetime.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;


/**
 * @Auther:wangchao
 * @Date: 2025/2/21-22:45
 * @Description: com.lifetime.api.entity
 * @Version:1.0
 */

@Data
@TableName("lt_api_sql_info")
public class ApiSqlInfoEntity extends BaseEntity {

    @ApiModelProperty(value = "接口编码",notes = "")
    @TableField("API_CODE")
    @NotEmpty(message = "接口编码不能空")
    private String apiCode ;

    @ApiModelProperty(value = "数据源ID")
    @TableField("DATA_SOURCE_ID")
    private String dataSourceId;

    @ApiModelProperty(value = "数据源类型")
    @TableField("DATA_SOURCE_TYPE")
    private String dataSourceType;


    @ApiModelProperty(value = "数据库schema")
    @TableField("SCHEMA_NAME")
    private String schemaName;

    @ApiModelProperty(value = "数据库表")
    @TableField("TABLE_NAME")
    private String tableName;

    @ApiModelProperty(value = "操作类型(查询,插入,更新,删除)")
    @TableField("OPERA_TYPE")
    private String operaType;
    @ApiModelProperty(value = "sql语句脚本")
    @TableField("SQL_SCRIPT")
    @Lob
    private String sqlScript;

}
