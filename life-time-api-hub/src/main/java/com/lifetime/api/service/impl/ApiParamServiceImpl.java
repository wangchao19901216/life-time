package com.lifetime.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.api.dao.ApiParamMapper;
import com.lifetime.api.dao.ApiSqlInfoMapper;
import com.lifetime.api.entity.ApiParamEntity;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.api.service.IApiParamService;
import com.lifetime.api.service.IApiSqlInfoService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ApiParamServiceImpl extends ServiceImpl<ApiParamMapper, ApiParamEntity> implements IApiParamService {
    @Autowired
    ApiParamMapper mapper;
    @Override
    public SearchResponse<ApiParamEntity> searchList(SearchRequest searchRequest) {
        QueryModel<ApiParamEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, ApiParamEntity.class);
        SearchResponse<ApiParamEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public boolean deleteByCode(String code) {
        return mapper.deleteByCode(code);
    }

    @Override
    public List<ApiParamEntity> getByApiCode(String apiCode) {
        LambdaQueryWrapper<ApiParamEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiParamEntity::getApiCode, apiCode);
        return mapper.selectList(queryWrapper);
    }
}