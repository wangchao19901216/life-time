package com.lifetime.common.manager.dao;

import com.lifetime.common.mapper.BaseDaoMapper;
import com.lifetime.common.manager.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseDaoMapper<UserEntity> {
}
