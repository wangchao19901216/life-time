package com.lifetime.common.manager.service;

import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.manager.entity.ThemeConfigEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:39
 * @description: com.lifetime.manager.service
 * @Version:1.0
 */
public interface IPermissionService extends BaseService<PermissionEntity> {

    PermissionEntity findByPermissionId(String permissionId);

    /**
     * flag =0 不过滤 状态查询
     * */
    List<PermissionEntity> childPermission(String permissionId,Integer flag);

}
