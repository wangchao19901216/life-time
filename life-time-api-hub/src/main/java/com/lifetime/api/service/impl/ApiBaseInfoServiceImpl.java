package com.lifetime.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.api.dao.ApiBaseInfoMapper;
import com.lifetime.api.dao.ApiGroupMapper;
import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.api.service.IApiBaseInfoService;
import com.lifetime.api.service.IApiGroupService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ApiBaseInfoServiceImpl extends ServiceImpl<ApiBaseInfoMapper, ApiBaseInfoEntity> implements IApiBaseInfoService {
    @Autowired
    ApiBaseInfoMapper mapper;
    @Override
    public SearchResponse<ApiBaseInfoEntity> searchList(SearchRequest searchRequest) {
        QueryModel<ApiBaseInfoEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, ApiBaseInfoEntity.class);
        SearchResponse<ApiBaseInfoEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public boolean isExist(String apiCode) {
        LambdaQueryWrapper<ApiBaseInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiBaseInfoEntity::getApiCode, apiCode);
        List<ApiBaseInfoEntity> list=mapper.selectList(queryWrapper);
        return  list.size()>0;
    }

    @Override
    public boolean deleteByCode(String code) {
        return mapper.deleteByCode(code);
    }
}