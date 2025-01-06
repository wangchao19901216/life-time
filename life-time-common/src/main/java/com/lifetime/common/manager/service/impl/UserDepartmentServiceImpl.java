package com.lifetime.common.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.manager.dao.UserDepartmentMapper;
import com.lifetime.common.manager.dao.UserDetailMapper;
import com.lifetime.common.manager.entity.UserDepartmentEntity;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.manager.service.IUserDepartmentService;
import com.lifetime.common.manager.service.IUserDetailService;
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
public class UserDepartmentServiceImpl extends ServiceImpl<UserDepartmentMapper, UserDepartmentEntity> implements IUserDepartmentService {
    @Autowired
    UserDepartmentMapper mapper;
    @Override
    public SearchResponse<UserDepartmentEntity> searchList(SearchRequest searchRequest) {
        QueryModel<UserDepartmentEntity> queryModel = QueryUtil.buildMyQuery(searchRequest, UserDepartmentEntity.class);
        SearchResponse<UserDepartmentEntity> searchResponse = QueryUtil.executeQuery(searchRequest, queryModel, this);
        return searchResponse;
    }


    @Override
    public List<UserDepartmentEntity> findByUserCode(String userCode) {
        LambdaQueryWrapper<UserDepartmentEntity> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDepartmentEntity::getUserCode,userCode);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public List<UserDepartmentEntity> findByDeptCode(String deptCode) {
        LambdaQueryWrapper<UserDepartmentEntity> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDepartmentEntity::getDepartmentCode,deptCode);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public boolean setActiveDept(String deptCode, String userCode) {
        List<UserDepartmentEntity> list=findByUserCode(userCode);
        for(UserDepartmentEntity entity:list){
            if(entity.getDepartmentCode().equals(deptCode)){
                entity.setActiveDept("1");
            }
            else{
                entity.setActiveDept("1");
            }
        }
        return super.updateBatchById(list);
    }
}
