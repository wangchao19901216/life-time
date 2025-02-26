package com.lifetime.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.api.dao.ApiGroupMapper;
import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.api.service.IApiGroupService;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.manager.dao.DepartmentMapper;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.service.IDepartmentService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ApiGroupServiceImpl extends ServiceImpl<ApiGroupMapper, ApiGroupEntity> implements IApiGroupService {
    @Autowired
    ApiGroupMapper mapper;
    @Override
    public SearchResponse<ApiGroupEntity> searchList(SearchRequest searchRequest) {
        QueryModel<ApiGroupEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, ApiGroupEntity.class);
        SearchResponse<ApiGroupEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public boolean isExist(String groupCode) {
        ApiGroupEntity entity= mapper.getApiGroupByCode(groupCode);
        if(entity==null)
        {
            return false;
        }
        else {
            return  true;
        }
    }

    @Override
    public List<ApiGroupEntity> childEntity(String groupCode) {
        LambdaQueryWrapper<ApiGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiGroupEntity::getParentCode, groupCode);
        return mapper.selectList(queryWrapper);
    }
}
