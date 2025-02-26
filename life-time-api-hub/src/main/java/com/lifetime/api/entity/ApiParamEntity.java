package com.lifetime.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


/**
 * @Auther:wangchao
 * @Date: 2025/2/21-22:45
 * @Description: com.lifetime.api.entity
 * @Version:1.0
 */

@Data
@TableName("lt_api_param")
public class ApiParamEntity extends BaseEntity {

    @ApiModelProperty(value = "接口编码",notes = "")
    @TableField("API_CODE")
    @NotEmpty(message = "接口编码不能空")
    private String apiCode ;




    @ApiModelProperty(value = "参数模型，request,response")
    @TableField("PARAM_MODEL")
    private String paramModel;


    @ApiModelProperty(value = "参数名称")
    @TableField("PARAM_NAME")
    private String paramName;

    @ApiModelProperty(value = "映射字段名")
    @TableField("COLUMN_NAME")
    private String columnName;

    @ApiModelProperty(value = "参数类型(String,Int,Date,Array,Boolean)")
    @TableField("PARAM_TYPE")
    private String paramType;

    @ApiModelProperty(value = "参数默认值")
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    @ApiModelProperty(value = "参数示例值")
    @TableField("EXAMPLE")
    private String example;

    @ApiModelProperty(value = "是否必填")
    @TableField("REQUIRED")
    private String required;

    @ApiModelProperty(value = "是否脱敏")
    @TableField("PRIVACY")
    private String privacy;

}
