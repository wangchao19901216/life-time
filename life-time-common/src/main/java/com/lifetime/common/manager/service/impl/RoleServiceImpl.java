package com.lifetime.common.manager.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.manager.dao.CodeMapper;
import com.lifetime.common.manager.dao.DepartmentMapper;
import com.lifetime.common.manager.dao.RoleMapper;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.entity.RoleEntity;
import com.lifetime.common.manager.service.ICodeService;
import com.lifetime.common.manager.service.IDepartmentService;
import com.lifetime.common.manager.service.IRoleService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2023/9/14-14:26
 * @description: com.general.common.base.service.impl
 * @Version:1.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements IRoleService {
    @Autowired
    RoleMapper  mapper;

    @Autowired
    IDepartmentService iDepartmentService;


    @Override
    public SearchResponse<RoleEntity> searchList(SearchRequest searchRequest) {
        QueryModel<RoleEntity> myQuery = QueryUtil.buildMyQuery(searchRequest,RoleEntity.class);
        SearchResponse<RoleEntity> searchResponse = QueryUtil.executeQuery(searchRequest,myQuery, this);
        return searchResponse;
    }

    @Override
    public boolean isExist(String roleCode) {
        RoleEntity entity=findByRoleCode(roleCode);
        if(LtCommonUtil.isNotBlankOrNull(entity))
        {
            return true;
        }
        else {
            return  false;
        }
    }
    @Override
    public RoleEntity findByRoleCode(String roleCode) {
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleEntity::getRoleCode, roleCode);
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public List<RoleEntity> childEntity(String roleCode) {
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleEntity::getRoleParentCode, roleCode);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public List<RoleEntity> getRoleTreeByDept(String deptCode) {
        DepartmentEntity departmentEntity=iDepartmentService.findByDeptCode(deptCode);
        String roles=departmentEntity.getDepartmentRolesTree();
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleEntity::getRoleCode, roles.split(","));
        return mapper.selectList(queryWrapper);
    }
}
