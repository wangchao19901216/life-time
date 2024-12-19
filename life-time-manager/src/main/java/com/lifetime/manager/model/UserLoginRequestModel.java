package com.lifetime.manager.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:wangchao
 * @date: 2024/12/18-13:53
 * @description: com.lifetime.manager.model
 * @Version:1.0
 */
@Data
public class UserLoginRequestModel implements Serializable {
    public String userCode;
    public String passWord;
}
