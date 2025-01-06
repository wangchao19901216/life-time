package com.lifetime.common.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.manager.dao.UserRoleMapper;
import com.lifetime.common.manager.entity.UserRoleEntity;
import com.lifetime.common.manager.service.IUserRoleService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Author wangchao
 * @Description
 * @Date 2024/12/17 13:26
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements IUserRoleService {
    @Autowired
    UserRoleMapper  mapper;
    @Override
    public SearchResponse<UserRoleEntity> searchList(SearchRequest searchRequest) {
        QueryModel<UserRoleEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, UserRoleEntity.class);
        SearchResponse<UserRoleEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }


    @Override
    public List<UserRoleEntity> findByUserCode(String userCode,String deptCode) {
        LambdaQueryWrapper<UserRoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleEntity::getUserCode, userCode);
        queryWrapper.eq(UserRoleEntity::getRoleDept, deptCode);
        queryWrapper.eq(UserRoleEntity::getStatus,1);
        return mapper.selectList(queryWrapper);

    }

    @Override
    public List<UserRoleEntity> findByRoleCode(String roleCode) {
        LambdaQueryWrapper<UserRoleEntity> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleEntity::getRoleCode, roleCode);
        queryWrapper.eq(UserRoleEntity::getStatus,1);
        return mapper.selectList(queryWrapper);
    }
}
