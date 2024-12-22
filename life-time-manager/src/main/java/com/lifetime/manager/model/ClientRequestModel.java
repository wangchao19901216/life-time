package com.lifetime.manager.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ClientRequestModel {

    @ApiModelProperty(value="客户端ID",required = true)
    // @NotEmpty(message = "clientId客户端ID不为空")
    public String clientId;

    // @NotEmpty(message = "clientSecret客户端密码不为空")
    @ApiModelProperty(value="客户端密码",required = true)
    public String clientSecret;

    @ApiModelProperty(value="可访问的资源服务器ID, 不写则不校验")
    // @NotEmpty(message = "resourceIds可访问的资源服务器ID 不能为空")
    public String resourceIds;


    @ApiModelProperty(value="客户端授权范围, 不指定默认不校验范围")
    //@NotEmpty(message = "scope客户端授权范围不为空,不效验请填写all")
    public String scope;

    // @NotEmpty(message = "authorizedGrantTypes 客户端授权类型不能为空")
    @ApiModelProperty(value="客户端授权类型，支持多个使用逗号分隔(authorization_code,password,implicit,client_credentials,refresh_token)")
    public String authorizedGrantTypes;


    @ApiModelProperty(value="服务器回调地址")
    public String uris;


    @ApiModelProperty(value="token 时长 默认 50000s")
    public int accessTokenValidity;

    @ApiModelProperty(value="token 刷新 默认 30天")
    public int refreshTokenValidity;

    @ApiModelProperty(value="附件信息")
    public String additionalInformation;


    @ApiModelProperty(value="false 显示授权点击页，true不显示自动授权")
    public String autoApprove;

}
