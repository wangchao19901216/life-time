package com.lifetime.api.dao;

import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiBaseInfoMapper extends BaseDaoMapper<ApiBaseInfoEntity> {

    @Delete("delete from lt_api_base_info where API_CODE=#{code}")
    boolean deleteByCode(String code);
}
