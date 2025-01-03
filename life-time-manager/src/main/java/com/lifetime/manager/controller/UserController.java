package com.lifetime.manager.controller;

import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.entity.UserDepartmentEntity;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.manager.entity.UserRoleEntity;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.service.ValidatedList;
import com.lifetime.manager.business.UserBusiness;
import com.lifetime.manager.model.UserLoginRequestModel;
import com.lifetime.manager.model.UserRequestModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/18-14:19
 * @description: com.lifetime.manager.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    UserBusiness  userBusiness;

    @PostMapping("login/{grant_type}")
    @ApiOperation(value = "用户登陆")
    public ResponseResult login(@PathVariable String grant_type, @RequestBody UserLoginRequestModel userLoginRequestModel) {
         return  userBusiness.loginDependOnRedis(grant_type,userLoginRequestModel);
    }


    @PostMapping("refresh/{refreshToken}")
    @ApiOperation(value = "刷新token")
    public ResponseResult refreshToken(@PathVariable String refreshToken) {
        return  userBusiness.refreshDependOnRedis(refreshToken);
    }

    @PostMapping("/login/encrypt/{grant_type}")
    @ApiOperation(value = "加密登入", notes = "")
    public ResponseResult loginEncrypt(@PathVariable String grant_type, @RequestBody UserLoginRequestModel userLoginDto) {
          return userBusiness.loginEncrypt(grant_type, userLoginDto);
    }

    @PostMapping("/getUserByToken")
    @ApiOperation(value = "用token获取用户信息", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult getUserByToken(Principal principal) {
        try {
            return ResponseResult.success(userBusiness.buildUserVo(null,principal.getName()));
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.INVALID_ACCESS_TOKEN);
        }
    }

    @GetMapping("/getUserByMobile/{mobile}")
    @ApiOperation(value = "用手机号取用户信息", notes = "")
    public ResponseResult getUserByPhone(@PathVariable String mobile) {
        try {
            return userBusiness.loginByMobile(mobile);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_ACCESS_FAILED);
        }
    }


    @PostMapping("")
    @ApiOperation(value = "新增", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult save(@Validated @RequestBody UserRequestModel userRequestModel) {
       try {
           return  userBusiness.add("",userRequestModel);
       }
       catch (Exception exception){
           return ResponseResult.error(500,exception.getMessage());
       }
    }

    @PostMapping("/{departmentCode}")
    @ApiOperation(value = "新增部门用户", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult save(@PathVariable String departmentCode,@Validated @RequestBody UserRequestModel userRequestModel) {
        try {
            return  userBusiness.add(departmentCode,userRequestModel);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }

    @PutMapping("{userCode}")
    @ApiOperation(value = "修改", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult update(@PathVariable String userCode, @RequestBody UserDetailEntity userDetailEntity) {
        try {
            return  userBusiness.update(userCode,userDetailEntity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }

    @DeleteMapping("{userCode}")
    @ApiOperation(value = "删除", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult delete(@PathVariable String userCode) {
        try {
            return  userBusiness.delete(userCode);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }


    @PostMapping("/update/password")
    @ApiOperation(value = "修改密码")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult updatePassword(@RequestBody UserLoginRequestModel userLoginRequestModel) {
        try {
            return userBusiness.updatePassWord(userLoginRequestModel);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_SEARCH_FAILED, exception.getMessage());
        }
    }

    @PostMapping("/check/password")
    @ApiOperation(value = "验证密码")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult checkPassword(@RequestBody UserLoginRequestModel userLoginRequestModel) {
        try {
            return  userBusiness.checkPassWord(userLoginRequestModel);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_SEARCH_FAILED, exception.getMessage());
        }
    }

    @PostMapping("/check/password/level")
    @ApiOperation(value = "查询密码强度")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult searchPWLevel(@RequestBody UserLoginRequestModel userLoginRequestModel) {
        try {
            return userBusiness.searchPWLevel(userLoginRequestModel);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_SEARCH_FAILED, exception.getMessage());
        }
    }
    @PostMapping("role/full")
    @ApiOperation(value = "用户角色(全量)", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult bindUserRole(@Validated @RequestBody List<UserRoleEntity> list) {
        try {
            return  userBusiness.bindUserRole(list);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }

    @GetMapping("role/{userCode}/{deptCode}")
    @ApiOperation(value = "用户角色", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult searchUserRole(@PathVariable String  userCode,@PathVariable String deptCode) {
        try {
            return  userBusiness.searchUserRole(userCode,deptCode);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }

    @PostMapping("role")
    @ApiOperation(value = "用户角色", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult saveUserRole(@Validated @RequestBody List<UserRoleEntity> list) {
        try {
            return  userBusiness.saveUserRole(list);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }


    @DeleteMapping("role/{id}")
    @ApiOperation(value = "用户角色", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult removeUserRole(@PathVariable BigInteger id) {
        try {
            return  userBusiness.removeUserRole(id);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }

    @PostMapping("department/full")
    @ApiOperation(value = "用户部门(全量)", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult save(@Validated @RequestBody ValidatedList<UserDepartmentEntity> list) {
        try {
            return  userBusiness.bindUserDept(list);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }


    @PostMapping("department")
    @ApiOperation(value = "用户部门", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult saveUserDept(@Validated @RequestBody ValidatedList<UserDepartmentEntity> list) {
        try {
            return  userBusiness.saveUserDept(list);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }
    @DeleteMapping("department/{id}")
    @ApiOperation(value = "用户部门", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult removeUserDept(@PathVariable BigInteger id) {
        try {
            return  userBusiness.removeUserDept(id);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }


    @GetMapping("department/{userCode}")
    @ApiOperation(value = "用户部门", notes = "")
    @PreAuthorize("hasAuthority('all')")
    public ResponseResult searchUserDept(@PathVariable String  userCode) {
        try {
            return  userBusiness.searchUserDept(userCode);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }


    @PostMapping("/search/{deptCode}")
    @ApiOperation(value = "查询(部门)")
    public ResponseResult searchByDepart(@PathVariable String deptCode,@RequestBody SearchRequest searchRequest) {
        try {
            return  userBusiness.searchByDepart(searchRequest,deptCode);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

    @PostMapping("/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return  userBusiness.search(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

}
