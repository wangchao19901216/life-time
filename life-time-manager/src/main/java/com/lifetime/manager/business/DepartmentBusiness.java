package com.lifetime.manager.business;

import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.enums.PermissionTypeEnum;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.manager.service.IDepartmentService;
import com.lifetime.common.manager.service.IPermissionService;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.LtModelUtil;
import com.lifetime.common.util.SnowflakeUtil;
import com.lifetime.manager.model.PermissionRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:wangchao
 * @date: 2024/12/19-20:52
 * @description: com.lifetime.manager.business
 * @Version:1.0
 */
@Component
public class DepartmentBusiness {
    @Autowired
    IDepartmentService iDepartmentService;

    public ResponseResult save(DepartmentEntity  departmentEntity) {
        DepartmentEntity resultEntity = iDepartmentService.findByDeptCode(departmentEntity.getDepartmentCode());
        if (LtCommonUtil.isNotBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "编号已经存在");
        } else {
            iDepartmentService.save(departmentEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult remove(String deptCode) {
        DepartmentEntity permissionEntity = iDepartmentService.findByDeptCode(deptCode);
        List<DepartmentEntity> departmentEntityList = iDepartmentService.childDept(deptCode, StatusConstants.DISABLE);
        if (departmentEntityList.size() > 0)
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "存在子部门，无法删除!");
        else {
            iDepartmentService.removeById(permissionEntity.getId());
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult update(String deptCode, DepartmentEntity departmentEntity) {
        DepartmentEntity resultEntity = iDepartmentService.findByDeptCode(deptCode);
        if (LtCommonUtil.isBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, "部门不存在");
        } else {
            departmentEntity.setDepartmentCode(deptCode);
            departmentEntity.setId(resultEntity.getId());
            iDepartmentService.updateById(departmentEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }
    public ResponseResult search(SearchRequest searchRequest) {
        return ResponseResult.success(iDepartmentService.searchList(searchRequest));
    }


}
