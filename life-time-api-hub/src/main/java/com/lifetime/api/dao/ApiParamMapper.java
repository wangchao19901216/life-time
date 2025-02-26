package com.lifetime.api.dao;

import com.lifetime.api.entity.ApiParamEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiParamMapper extends BaseDaoMapper<ApiParamEntity> {

    @Delete("delete from lt_api_param where API_CODE=#{code}")
    boolean deleteByCode(String code);
}
