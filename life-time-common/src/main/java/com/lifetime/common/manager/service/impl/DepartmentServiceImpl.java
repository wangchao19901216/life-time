package com.lifetime.common.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.manager.dao.DepartmentMapper;
import com.lifetime.common.manager.dao.PermissionMapper;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.manager.service.IDepartmentService;
import com.lifetime.common.manager.service.IPermissionService;
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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentEntity> implements IDepartmentService {
    @Autowired
    DepartmentMapper mapper;

    @Override
    public SearchResponse<DepartmentEntity> searchList(SearchRequest searchRequest) {
        QueryModel<DepartmentEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, DepartmentEntity.class);
        SearchResponse<DepartmentEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public DepartmentEntity findByDeptCode(String deptCode) {
        LambdaQueryWrapper<DepartmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DepartmentEntity::getDepartmentCode, deptCode);
        queryWrapper.eq(DepartmentEntity::getStatus, 1);
        List<DepartmentEntity> permissionEntityList = mapper.selectList(queryWrapper);
        if(permissionEntityList.size()>0){
            return  permissionEntityList.get(0);
        }
        return null;
    }

    @Override
    public List<DepartmentEntity> childDept(String deptCode, Integer flag) {
        LambdaQueryWrapper<DepartmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DepartmentEntity::getDepartmentCode, deptCode);
        if(flag==StatusConstants.ENABLE){
            queryWrapper.eq(DepartmentEntity::getStatus, StatusConstants.ENABLE);
        }
        return mapper.selectList(queryWrapper);
    }
}
