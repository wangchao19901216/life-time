package com.lifetime.common.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.manager.dao.UserDetailMapper;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.util.QueryUtil;
import com.lifetime.common.manager.dao.UserMapper;
import com.lifetime.common.manager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Author wangchao
 * @Description
 * @Date 2024/12/17 13:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public SearchResponse<UserEntity> searchList(SearchRequest searchRequest) {
        QueryModel<UserEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, UserEntity.class);
        SearchResponse<UserEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public UserEntity findByUserCode(String userCode) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, userCode);
        UserEntity userEntity = userMapper.selectOne(queryWrapper);
        if (userEntity != null) {
            userEntity.setAuthorities("all");
        }
        return userEntity;
    }
    @Override
    public UserEntity findByMobile(String mobile) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getMobile, mobile);
        List<UserEntity> userEntityList = userMapper.selectList(queryWrapper);
        if(userEntityList.size()>0){
            UserEntity userEntity = userEntityList.get(0);
            userEntity.setAuthorities("all");
            return userEntity;
        }
        return null;
    }

    @Override
    public Integer updatePassword(String userCode, String pw) {
        return userMapper.updatePassword(userCode,pw);
    }

    @Override
    public Integer updateStatus(String userCode, Integer status) {
        return userMapper.updateStatus(userCode,status);
    }

    @Override
    public Boolean checkPassword(String userCode, String passWord) {
        UserEntity userEntity= findByUserCode(userCode);
        return passwordEncoder.matches(passWord,userEntity.getPassword());
    }
}
