package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.UserDepartmentEntity;
import com.lifetime.common.manager.entity.UserRoleEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author:wangchao
 * @date: 2024/12/25-10:26
 * @description: com.lifetime.common.manager.dao
 * @Version:1.0
 */
@Mapper
public interface UserRoleMapper extends BaseDaoMapper<UserRoleEntity> {
}
