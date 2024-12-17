package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDetailMapper extends BaseDaoMapper<UserDetailEntity> {
}
