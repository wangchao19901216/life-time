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
@TableName("lt_api_base_info")
public class ApiBaseInfoEntity extends BaseEntity {

    @ApiModelProperty(value = "接口编码",notes = "")
    @TableField("API_CODE")
    @NotEmpty(message = "接口编码不能空")
    private String apiCode ;

    @ApiModelProperty(value = "接口名称")
    @TableField("API_NAME")
    @NotEmpty(message = "接口名称不能空")
    private String apiName;

    @ApiModelProperty(value = "接口地址")
    @TableField("API_URL")
    private String apiUrl;

    @ApiModelProperty(value = "分组编码")
    @TableField("GROUP_CODE")
    private String groupCode;

    @ApiModelProperty(value = "分组名称")
    private transient String groupName;

    @ApiModelProperty(value = "请求方式 GET、POST、PUT、DELETE、PATCH")
    @TableField("API_METHOD")
    private String apiMethod;
    @ApiModelProperty(value = "接口类型(SQL脚本,表格模式)")
    @TableField("API_TYPE")
    private String apiType;

    @ApiModelProperty(value = "认证方式(无认证,签名认证,oauth2认证)")
    @TableField("AUTH_TYPE")
    private String authType;

    @ApiModelProperty(value = "权限级别(公开-所以人都能看,私有-仅自己能看)")
    @TableField("PERMISSION_LEVEL")
    private String permissionLevel;

    @ApiModelProperty(value = "版本号")
    @TableField("VERSION_NUMBER")
    private String versionNumber;

    @ApiModelProperty(value = "接口描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "是否发布")
    @TableField("IS_PUBLISH")
    private String isPublish;
    @ApiModelProperty(value = "是否分页")
    @TableField("IS_PAGE")
    private String isPage;

}
