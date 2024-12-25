package com.lifetime.common.manager.service;

import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:39
 * @description: com.lifetime.manager.service
 * @Version:1.0
 */
public interface IDepartmentService extends BaseService<DepartmentEntity> {
    DepartmentEntity findByDeptCode(String deptCode);

    List<DepartmentEntity> findByDeptCodeArray(String[] deptCode);

    /**
     * flag =0 不过滤 状态查询
     * */
    List<DepartmentEntity> childDept(String deptCode, Integer flag);

}
