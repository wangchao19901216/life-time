package com.lifetime.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author:wangchao
 * @date: 2025/2/25-15:23
 * @description: com.lifetime.api.entity
 * @Version:1.0
 */
@Data
@TableName("lt_api_app")
public class AppEntity extends BaseEntity {
    @ApiModelProperty(value = "应用编码",notes = "")
    @TableField("APP_ID")
    @NotEmpty(message = "应用不能空")
    private String appId;

    @ApiModelProperty(value = "应用名称",notes = "")
    @TableField("APP_NAME")
    private String appName;

    @ApiModelProperty(value = "应用描述",notes = "")
    @TableField("APP_DES")
    private String appDes;

    @ApiModelProperty(value = "认证code",notes = "")
    @TableField("APP_CODE")
    private String appCode;

    @ApiModelProperty(value = "密钥ID",notes = "")
    @TableField("APP_KEY")
    private String appKey;

    @ApiModelProperty(value = "密钥ID",notes = "")
    @TableField("APP_SECRET")
    private String appSecret;

    @ApiModelProperty(value = "策略类型：白名单：white、黑名单black",notes = "")
    @TableField("STRATEGY")
    private String strategy;

    @ApiModelProperty(value = "ip地址，分号分割",notes = "")
    @TableField("IPS")
    private String ips;


    @ApiModelProperty(value = "关联的apiId",notes = "")
    @TableField("API_CODES")
    @Lob
    private String apiCodes;

}
