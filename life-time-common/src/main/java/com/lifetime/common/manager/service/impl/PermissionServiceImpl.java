package com.lifetime.common.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.manager.dao.PermissionMapper;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.manager.entity.UserEntity;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements IPermissionService {
    @Autowired
    PermissionMapper mapper;

    @Override
    public SearchResponse<PermissionEntity> searchList(SearchRequest searchRequest) {
        QueryModel<PermissionEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, PermissionEntity.class);
        SearchResponse<PermissionEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public PermissionEntity findByPermissionId(String permissionId) {
        LambdaQueryWrapper<PermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionEntity::getPermissionId, permissionId);
        queryWrapper.eq(PermissionEntity::getStatus, 1);
        List<PermissionEntity> permissionEntityList = mapper.selectList(queryWrapper);
        if(permissionEntityList.size()>0){
            return  permissionEntityList.get(0);
        }
        return null;
    }
    @Override
    public List<PermissionEntity> childPermission(String permissionId,Integer flag) {
        LambdaQueryWrapper<PermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionEntity::getParentId, permissionId);
        if(flag==StatusConstants.ENABLE){
            queryWrapper.eq(PermissionEntity::getStatus, StatusConstants.ENABLE);
        }
        return mapper.selectList(queryWrapper);
    }
}
