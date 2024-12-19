package com.lifetime.common.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.manager.dao.ThemeConfigMapper;
import com.lifetime.common.manager.dao.ThemeStyleMapper;
import com.lifetime.common.manager.entity.ThemeConfigEntity;
import com.lifetime.common.manager.entity.ThemeStyleEntity;
import com.lifetime.common.manager.service.IThemeConfigService;
import com.lifetime.common.manager.service.IThemeStyleService;
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
public class ThemeStyleServiceImpl extends ServiceImpl<ThemeStyleMapper, ThemeStyleEntity> implements IThemeStyleService {
    @Autowired
    ThemeStyleMapper mapper;

    @Override
    public SearchResponse<ThemeStyleEntity> searchList(SearchRequest searchRequest) {
        QueryModel<ThemeStyleEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, ThemeStyleEntity.class);
        SearchResponse<ThemeStyleEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }
}
