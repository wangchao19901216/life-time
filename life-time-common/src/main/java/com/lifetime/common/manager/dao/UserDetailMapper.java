package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDetailMapper extends BaseDaoMapper<UserDetailEntity> {
    @Update("update LT_USER_DETAIL set status=#{status} where USER_CODE=#{userCode}")
    Integer updateStatus(String userCode, Integer status);
}
