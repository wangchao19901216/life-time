package com.lifetime.common.manager.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserDetailMapper extends BaseDaoMapper<UserDetailEntity> {
    @Update("update LT_M_USER_DETAIL set status=#{status} where USER_CODE=#{userCode}")
    Integer updateStatus(String userCode, Integer status);


    @Select("select * from lt_m_user_detail where  USER_CODE in(select USER_CODE from lt_m_user_department where DEPARTMENT_CODE=#{deptCode})")
    IPage<UserDetailEntity> getUserByDeptCode(Page<UserDetailEntity> page,String deptCode);
}
