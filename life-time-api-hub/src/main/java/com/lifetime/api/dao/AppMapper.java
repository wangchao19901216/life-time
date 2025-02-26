package com.lifetime.api.dao;

import com.lifetime.api.entity.AppEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppMapper extends BaseDaoMapper<AppEntity> {

}
