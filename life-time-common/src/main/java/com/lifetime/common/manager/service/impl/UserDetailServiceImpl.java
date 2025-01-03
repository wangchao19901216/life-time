package com.lifetime.common.manager.service.impl;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.manager.dao.UserDetailMapper;
import com.lifetime.common.manager.dao.UserMapper;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.manager.service.IUserDetailService;
import com.lifetime.common.manager.service.IUserService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Author wangchao
 * @Description
 * @Date 2024/12/17 13:26
 */
@Service
public class UserDetailServiceImpl extends ServiceImpl<UserDetailMapper, UserDetailEntity> implements IUserDetailService {
    @Autowired
    UserDetailMapper userDetailMapper;

    @Override
    public SearchResponse<UserDetailEntity> searchList(SearchRequest searchRequest) {
        QueryModel<UserDetailEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, UserDetailEntity.class);
        SearchResponse<UserDetailEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }

    @Override
    public UserDetailEntity findByUserCode(String userCode) {
        LambdaQueryWrapper<UserDetailEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDetailEntity::getUserCode, userCode);
        UserDetailEntity userDetailEntity = userDetailMapper.selectOne(queryWrapper);
        return userDetailEntity;
    }

    @Override
    public UserDetailEntity findByMobile(String mobile) {
        LambdaQueryWrapper<UserDetailEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDetailEntity::getMobile, mobile);
        List<UserDetailEntity> userEntityList = userDetailMapper.selectList(queryWrapper);
        if(userEntityList.size()>0){
            UserDetailEntity userEntity = userEntityList.get(0);
            return userEntity;
        }
        return null;
    }

    @Override
    public SearchResponse<UserDetailEntity> findByDept(SearchRequest searchRequest,String deptCode) {
        QueryModel<UserDetailEntity> myQuery = QueryUtil.buildMyQuery(searchRequest,UserDetailEntity.class);
        SearchResponse<UserDetailEntity> searchResponse=new SearchResponse<>();
        searchResponse.pageInfo = searchRequest.pageParams;
        IPage<UserDetailEntity> iPage=userDetailMapper.getUserByDeptCode(myQuery.getPage(),deptCode);
        searchResponse.results = iPage.getRecords();
        searchResponse.pageInfo.total = (int) iPage.getTotal();
        return searchResponse;
    }

    @Override
    public Integer updateStatus(String userCode, Integer status) {
        return userDetailMapper.updateStatus(userCode,status);
    }
}
