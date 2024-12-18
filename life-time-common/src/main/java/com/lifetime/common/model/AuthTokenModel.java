package com.lifetime.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:wangchao
 * @date: 2024/12/18-13:59
 * @description: com.lifetime.manager.model
 * @Version:1.0
 */
@Data
public class AuthTokenModel implements Serializable {

    public String access_token;
    public String token_type;
    public String refresh_token;
    public String expires_in;
    public String scope;
}
