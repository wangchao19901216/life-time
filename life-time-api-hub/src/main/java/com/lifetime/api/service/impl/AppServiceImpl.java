package com.lifetime.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.api.dao.ApiSqlInfoMapper;
import com.lifetime.api.dao.AppMapper;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.api.entity.AppEntity;
import com.lifetime.api.service.IApiSqlInfoService;
import com.lifetime.api.service.IAppService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, AppEntity> implements IAppService {
    @Autowired
    AppMapper mapper;
    @Override
    public SearchResponse<AppEntity> searchList(SearchRequest searchRequest) {
        QueryModel<AppEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, AppEntity.class);
        SearchResponse<AppEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public List<AppEntity> getByApiCode(String code) {
        LambdaQueryWrapper<AppEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(AppEntity::getApiCodes, code);
        return mapper.selectList(queryWrapper);
    }
}