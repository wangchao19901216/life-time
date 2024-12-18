package com.lifetime.manager.model;

import com.lifetime.common.manager.entity.UserDetailEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author:wangchao
 * @date: 2024/12/18-13:53
 * @description: com.lifetime.manager.model
 * @Version:1.0
 */
@Data
public class UserRequestModel extends UserDetailEntity implements  Serializable{
    private String passWord;
}
