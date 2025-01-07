package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.RoleEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/24-14:08
 * @description: com.lifetime.common.manager.dao
 * @Version:1.0
 */
@Mapper
public interface RoleMapper extends BaseDaoMapper<RoleEntity> {



    @Select("select * from LT_M_ROLE WHERE ROLE_TYPE='1' AND ROLE_CODE IN(select ROLE_CODE from lt_m_user_role where USER_CODE=#{userCode} and ROLE_DEPT=#{dept} ) ")
    List<RoleEntity> getRoleListByUserCodeAndDept(String userCode,String dept);
}
