package com.lifetime.api.dao;

import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiSqlInfoMapper extends BaseDaoMapper<ApiSqlInfoEntity> {
    @Delete("delete from lt_api_sql_info where API_CODE=#{code}")
    boolean deleteByCode(String code);
}
