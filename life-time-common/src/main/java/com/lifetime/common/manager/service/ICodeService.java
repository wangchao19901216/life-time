package com.lifetime.common.manager.service;


import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:39
 * @description: com.lifetime.manager.service
 * @Version:1.0
 */
public interface ICodeService extends BaseService<CodeEntity> {
    boolean isExist(String codeParent,String code);


    List<CodeEntity> childEntity(String codeType, String codeValue);
}
