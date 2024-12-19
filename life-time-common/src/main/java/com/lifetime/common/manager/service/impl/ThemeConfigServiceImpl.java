package com.lifetime.common.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.manager.dao.ThemeConfigMapper;
import com.lifetime.common.manager.entity.ThemeConfigEntity;
import com.lifetime.common.manager.service.IThemeConfigService;
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
public class ThemeConfigServiceImpl extends ServiceImpl<ThemeConfigMapper, ThemeConfigEntity> implements IThemeConfigService {
    @Autowired
    ThemeConfigMapper mapper;

    @Override
    public SearchResponse<ThemeConfigEntity> searchList(SearchRequest searchRequest) {
        QueryModel<ThemeConfigEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, ThemeConfigEntity.class);
        SearchResponse<ThemeConfigEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }
    @Override
    public Integer updateByKey(String key, String value) {
        return mapper.updateByKey(key,value);
    }

    @Override
    public List<ThemeConfigEntity> getAllThemeConfig() {
        return mapper.getAllThemeConfig();
    }
}
