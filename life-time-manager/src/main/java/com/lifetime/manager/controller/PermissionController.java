package com.lifetime.manager.controller;

import com.lifetime.common.annotation.RepeatSubmit;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.manager.entity.ThemeStyleEntity;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.manager.business.PermissionBusiness;
import com.lifetime.manager.business.ThemeBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author:wangchao
 * @date: 2024/12/18-14:19
 * @description: com.lifetime.manager.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("permission")
@Api(tags = "权限管理")
@PreAuthorize("hasAuthority('all')")
public class PermissionController {
    @Autowired
    PermissionBusiness permissionBusiness;
    @PostMapping("")
    @ApiOperation(value = "增加")
    public ResponseResult save(@Validated @RequestBody PermissionEntity permissionEntity) {
        try {
            return  permissionBusiness.save(permissionEntity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED);
        }
    }
    @DeleteMapping("/{permissionId}")
    @ApiOperation(value = "删除")
    public ResponseResult delete(@PathVariable String permissionId) {
        try {
            return  permissionBusiness.remove(permissionId);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }
    @PutMapping("/{permissionId}")
    @ApiOperation(value = "修改")
    @RepeatSubmit()
    public ResponseResult update(@PathVariable String  permissionId,@RequestBody PermissionEntity permissionEntity) {
        try {
            return  permissionBusiness.update(permissionId,permissionEntity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }


    @PostMapping("/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return ResponseResult.success("成功",permissionBusiness.search(searchRequest));
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }


}
