package com.lifetime.common.manager.service;

import com.lifetime.common.manager.entity.ThemeConfigEntity;
import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:39
 * @description: com.lifetime.manager.service
 * @Version:1.0
 */
public interface IThemeConfigService extends BaseService<ThemeConfigEntity> {

    Integer updateByKey(String key, String value);

    List<ThemeConfigEntity> getAllThemeConfig();
}
