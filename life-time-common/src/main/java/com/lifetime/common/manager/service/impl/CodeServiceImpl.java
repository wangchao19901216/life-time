package com.lifetime.common.manager.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.manager.dao.CodeMapper;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.service.ICodeService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2023/9/14-14:26
 * @description: com.general.common.base.service.impl
 * @Version:1.0
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, CodeEntity> implements ICodeService {
    @Autowired
    CodeMapper  codeMapper;
    @Override
    public boolean save(CodeEntity codeEntity) {
        return super.save(codeEntity);
    }



    @Override
    public SearchResponse<CodeEntity> searchList(SearchRequest searchRequest) {
        QueryModel<CodeEntity> myQuery = QueryUtil.buildMyQuery(searchRequest,CodeEntity.class);
        SearchResponse<CodeEntity> searchResponse = QueryUtil.executeQuery(searchRequest,myQuery, this);
        return searchResponse;
    }

    @Override
    public boolean isExist(String typeCode, String code) {
        CodeEntity entity= codeMapper.isExist(typeCode,code);
        if(entity==null)
        {
            return false;
        }
        else {
            return  true;
        }
    }

    @Override
    public List<CodeEntity> childEntity(String codeType, String codeValue) { 
        LambdaQueryWrapper<CodeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CodeEntity::getCodeType, codeType);
        queryWrapper.eq(CodeEntity::getCodeParent,codeValue);
        return codeMapper.selectList(queryWrapper);
    }
}
