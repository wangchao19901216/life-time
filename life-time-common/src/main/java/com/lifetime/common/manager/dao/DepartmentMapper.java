package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseDaoMapper<DepartmentEntity> {


}