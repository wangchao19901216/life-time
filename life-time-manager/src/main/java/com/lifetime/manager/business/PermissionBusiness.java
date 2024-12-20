package com.lifetime.manager.business;

import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.manager.service.IPermissionService;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.LtModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/19-20:52
 * @description: com.lifetime.manager.business
 * @Version:1.0
 */
@Component
public class PermissionBusiness {
    @Autowired
    IPermissionService iPermissionService;

    public ResponseResult save(PermissionEntity permissionEntity){
        PermissionEntity resultEntity=iPermissionService.findByPermissionId(permissionEntity.getPermissionId());
        if(LtCommonUtil.isNotBlankOrNull(resultEntity)){
            return  ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED,"编号已经存在");
        }
        else {
            iPermissionService.save(permissionEntity);
            return  ResponseResult.success(ResponseResultConstants.SUCCESS);
        }

    }

    public ResponseResult remove(String permissionId){
        PermissionEntity permissionEntity=iPermissionService.findByPermissionId(permissionId);
        List<PermissionEntity> permissionEntityList=iPermissionService.childPermission(permissionId, StatusConstants.DISABLE);
        if(permissionEntityList.size()>0)
            return  ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED,"存在子菜单，无法删除!");
        else{
            iPermissionService.removeById(permissionEntity.getId());
            return  ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }
    public ResponseResult update(String permissionId,PermissionEntity permissionEntity){
        PermissionEntity resultEntity=iPermissionService.findByPermissionId(permissionId);
        if(LtCommonUtil.isBlankOrNull(resultEntity)){
            return  ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED,"权限不存在");
        }
        else{
            permissionEntity.setPermissionId(permissionId);
            permissionEntity.setId(resultEntity.getId());
            iPermissionService.updateById(permissionEntity);
            return  ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult search(SearchRequest searchRequest){
        return  ResponseResult.success(iPermissionService.searchList(searchRequest));
    }


}
