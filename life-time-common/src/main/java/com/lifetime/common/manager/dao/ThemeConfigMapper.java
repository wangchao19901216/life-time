package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.ThemeConfigEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThemeConfigMapper extends BaseDaoMapper<ThemeConfigEntity> {

    @Select("update  LT_T_THEME_CONFIG set THEME_VALUE=#{value} where THEME_KEY=#{key}")
    Integer updateByKey(String key, String value);
    @Select("select * from  LT_T_THEME_CONFIG where STATUS=1")
    List<ThemeConfigEntity> getAllThemeConfig();

}
