package com.lifetime.common.manager.dao;


import com.lifetime.common.manager.entity.ClientEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientMapper extends BaseDaoMapper<ClientEntity> {
}
