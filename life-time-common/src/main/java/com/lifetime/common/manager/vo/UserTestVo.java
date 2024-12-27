package com.lifetime.common.manager.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.model.AuthTokenModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:18
 * @description: com.lifetime.manager.vo
 * @Version:1.0
 */
@Data
public class UserTestVo implements Serializable {

     public String access_token;
     public String activeDept;

}
