package com.lifetime.common.manager.service;

import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.service.BaseService;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:39
 * @description: com.lifetime.manager.service
 * @Version:1.0
 */
public interface IUserService extends BaseService<UserEntity> {
    UserEntity findByUserCode(String userCode);
    UserEntity findByMobile(String mobile);
    Integer updatePassword(String userCode, String pw);
    Integer updateStatus(String userCode, Integer status);

    Boolean checkPassword(String userCode,String passWord);
}
