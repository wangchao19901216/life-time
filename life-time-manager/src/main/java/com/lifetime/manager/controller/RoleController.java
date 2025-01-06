package com.lifetime.manager.controller;

import com.lifetime.common.annotation.RepeatSubmit;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.RoleEntity;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.manager.business.CodeBusiness;
import com.lifetime.manager.business.RoleBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.math.BigInteger;

/**
 * @author:wangchao
 * @date: 2024/12/24-15:44
 * @description: com.lifetime.manager.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("role")
@Api(tags = "角色管理")
@PreAuthorize("hasAuthority('all')")
public class RoleController {
    @Autowired
    RoleBusiness roleBusiness;

    @PostMapping("")
    @ApiOperation(value = "增加")
    public ResponseResult save(@Validated @RequestBody RoleEntity entity) {
        try {
            return  roleBusiness.save(entity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public ResponseResult delete(@PathVariable BigInteger id) {
        try {
            return  roleBusiness.remove(id);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }
    @PutMapping("/{id}")
    @ApiOperation(value = "修改")
    @RepeatSubmit()
    public ResponseResult update(@PathVariable BigInteger id, @RequestBody RoleEntity entity) {
        try {
            return  roleBusiness.update(id,entity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }


    @PostMapping("/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return  roleBusiness.search(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

    @PostMapping("/tree")
    @ApiOperation(value = "查询-树型")
    public ResponseResult tree(@RequestBody SearchRequest searchRequest) {
        try {
            return  roleBusiness.getTree(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

    @GetMapping("/tree/{deptCode}")
    @ApiOperation(value = "查询部门角色-树型")
    public ResponseResult tree(@PathVariable String deptCode) {
        try {
            return  roleBusiness.getTreeByDept(deptCode);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }
}

