package com.lifetime.manager.controller;

import com.lifetime.common.annotation.RepeatSubmit;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.manager.business.DepartmentBusiness;
import com.lifetime.manager.business.PermissionBusiness;
import com.lifetime.manager.model.PermissionRequestModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author:wangchao
 * @date: 2024/12/18-14:19
 * @description: com.lifetime.manager.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("department")
@Api(tags = "部门管理")
@PreAuthorize("hasAuthority('all')")
public class DepartmentController {
    @Autowired
    DepartmentBusiness departmentBusiness;
    @PostMapping("")
    @ApiOperation(value = "增加")
    public ResponseResult save(@Validated @RequestBody DepartmentEntity entity) {
        try {
            return  departmentBusiness.save(entity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED);
        }
    }

    @DeleteMapping("/{deptCode}")
    @ApiOperation(value = "删除")
    public ResponseResult delete(@PathVariable String deptCode) {
        try {
            return  departmentBusiness.remove(deptCode);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }
    @PutMapping("/{deptCode}")
    @ApiOperation(value = "修改")
    @RepeatSubmit()
    public ResponseResult update(@PathVariable String  deptCode,@RequestBody DepartmentEntity entity) {
        try {
            return  departmentBusiness.update(deptCode,entity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }


    @PostMapping("/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return  departmentBusiness.search(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

//
//    @PostMapping("/search/{permissionId}")
//    @ApiOperation(value = "查询")
//    public ResponseResult searchOneWithButton(@PathVariable String  permissionId) {
//        try {
//            return  permissionBusiness.searchOneWithButton(permissionId);
//        } catch (Exception exception) {
//            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
//        }
//    }
//

}
