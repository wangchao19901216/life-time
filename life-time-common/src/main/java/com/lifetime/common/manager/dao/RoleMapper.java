package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.RoleEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.management.relation.Role;

/**
 * @author:wangchao
 * @date: 2024/12/24-14:08
 * @description: com.lifetime.common.manager.dao
 * @Version:1.0
 */
@Mapper
public interface RoleMapper extends BaseDaoMapper<RoleEntity> {

}
