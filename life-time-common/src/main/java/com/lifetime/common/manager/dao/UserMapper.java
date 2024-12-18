package com.lifetime.common.manager.dao;

import com.lifetime.common.mapper.BaseDaoMapper;
import com.lifetime.common.manager.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseDaoMapper<UserEntity> {

    @Select("update  LT_USER set PASSWORD=#{pw} where USER_CODE=#{userCode}")
    Integer updatePassword(String userCode, String pw);


    @Update("update LT_USER set status=#{status} where USER_CODE=#{userCode}")
    Integer updateStatus(String userCode, Integer status);


}
