package com.lifetime.common.manager.service;

import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.service.BaseService;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:39
 * @description: com.lifetime.manager.service
 * @Version:1.0
 */
public interface IUserDetailService extends BaseService<UserDetailEntity> {

    UserDetailEntity findByUserCode(String userCode);
    UserDetailEntity findByMobile(String mobile);

    SearchResponse<UserDetailEntity> findByDept(SearchRequest searchRequest, String deptCode);
    Integer updateStatus(String userCode, Integer status);
}
