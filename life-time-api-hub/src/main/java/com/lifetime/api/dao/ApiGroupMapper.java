package com.lifetime.api.dao;

import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApiGroupMapper extends BaseDaoMapper<ApiGroupEntity> {
    @Select("select * from lt_api_group  where GROUP_CODE=#{groupCode}")
    ApiGroupEntity getApiGroupByCode(String groupCode);
}
