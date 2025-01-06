package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.UserDepartmentEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/25-10:26
 * @description: com.lifetime.common.manager.dao
 * @Version:1.0
 */
@Mapper
public interface UserDepartmentMapper  extends BaseDaoMapper<UserDepartmentEntity> {


    @Select("select A.*,B.DEPARTMENT_NAME from lt_m_user_department A left join lt_m_department B on A.DEPARTMENT_CODE=B.DEPARTMENT_CODE WHERE A.USER_CODE=#{userCode}")
    List<UserDepartmentEntity> getUserDepartmentByUserCode(String userCode);
}
