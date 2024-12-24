package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Auther:wangchao
 * @Date: 2024/12/20-22:45
 * @Description: com.lifetime.common.manager.entity
 * @Version:1.0
 */

@Data
@TableName("oauth_client_details")
public class ClientEntity implements Serializable {

    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(value="客户端ID",required = true)
    @TableField("client_id")
   // @NotEmpty(message = "clientId客户端ID不为空")
    public String clientId;

   // @NotEmpty(message = "clientSecret客户端密码不为空")
    @ApiModelProperty(value="客户端密码",required = true)
    @TableField("client_secret")
    public String clientSecret;

    @ApiModelProperty(value="可访问的资源服务器ID, 不写则不校验")
    @TableField("resource_ids")
   // @NotEmpty(message = "resourceIds可访问的资源服务器ID 不能为空")
    public String resourceIds;


    @ApiModelProperty(value="客户端授权范围, 不指定默认不校验范围")
    //@NotEmpty(message = "scope客户端授权范围不为空,不效验请填写all")
    @TableField("scope")
    public String scope;

   // @NotEmpty(message = "authorizedGrantTypes 客户端授权类型不能为空")
    @ApiModelProperty(value="客户端授权类型，支持多个使用逗号分隔(authorization_code,password,implicit,client_credentials,refresh_token)")
    @TableField("authorized_grant_types")
    public String authorizedGrantTypes;


    @ApiModelProperty(value="服务器回调地址")
    @TableField("web_server_redirect_uri")
    public String uris;


    @ApiModelProperty(value="token 时长 默认 50000s")
    @TableField("access_token_validity")
    public int accessTokenValidity;

    @ApiModelProperty(value="token 刷新 默认 30天")
    @TableField("refresh_token_validity")
    public int refreshTokenValidity;

    @ApiModelProperty(value="附件信息")
    @TableField("additional_information")
    public String additionalInformation;


    @ApiModelProperty(value="false 显示授权点击页，true不显示自动授权")
    @TableField("autoapprove")
    public String autoApprove;


}
