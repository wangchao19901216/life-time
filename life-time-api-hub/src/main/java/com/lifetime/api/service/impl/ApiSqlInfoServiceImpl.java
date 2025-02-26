package com.lifetime.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.api.dao.ApiBaseInfoMapper;
import com.lifetime.api.dao.ApiSqlInfoMapper;
import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.api.service.IApiBaseInfoService;
import com.lifetime.api.service.IApiSqlInfoService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ApiSqlInfoServiceImpl extends ServiceImpl<ApiSqlInfoMapper, ApiSqlInfoEntity> implements IApiSqlInfoService {
    @Autowired
    ApiSqlInfoMapper mapper;
    @Override
    public SearchResponse<ApiSqlInfoEntity> searchList(SearchRequest searchRequest) {
        QueryModel<ApiSqlInfoEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, ApiSqlInfoEntity.class);
        SearchResponse<ApiSqlInfoEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public boolean deleteByCode(String code) {
        return mapper.deleteByCode(code);
    }

    @Override
    public ApiSqlInfoEntity getByApiCode(String code) {
        LambdaQueryWrapper<ApiSqlInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiSqlInfoEntity::getApiCode, code);
        List<ApiSqlInfoEntity> list= mapper.selectList(queryWrapper);
        return list.size()>0?list.get(0):null;
    }
}