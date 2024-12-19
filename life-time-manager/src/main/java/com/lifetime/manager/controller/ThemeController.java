package com.lifetime.manager.controller;

import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.ThemeStyleEntity;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.manager.business.ThemeBusiness;
import com.lifetime.manager.business.UserBusiness;
import com.lifetime.manager.model.UserLoginRequestModel;
import com.lifetime.manager.model.UserRequestModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author:wangchao
 * @date: 2024/12/18-14:19
 * @description: com.lifetime.manager.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("theme")
@Api(tags = "系统样式设置")
@PreAuthorize("hasAuthority('all')")
public class ThemeController {
    @Autowired
    ThemeBusiness themeBusiness;

    @GetMapping("config")
    @ApiOperation(value = "专题配置文件")
    public ResponseResult getThemeConfig() {
        try {
            return  themeBusiness.getThemeConfig();
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SEARCH_FAILED);
        }

    }

    @PutMapping("config")
    @ApiOperation(value = "修改专题配置文件")
    public ResponseResult updateBatch(@RequestBody Map<String, Object> map) {
        try {
            return  themeBusiness.updateBatch(map);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }

    @GetMapping("style")
    @ApiOperation(value = "专题样式文件")
    public ResponseResult getThemeStyle(@RequestBody SearchRequest searchRequest) {
        try {
            return   themeBusiness.getThemeStyle(searchRequest);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SEARCH_FAILED);
        }
    }

    @PostMapping("style")
    @ApiOperation(value = "专题样式文件")
    public ResponseResult saveThemeStyle(@RequestBody List<ThemeStyleEntity> themeStyleEntityList) {
        try {
            return  themeBusiness.saveThemeStyle(themeStyleEntityList);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED);
        }
    }
    @DeleteMapping("style/{id}")
    @ApiOperation(value = "专题样式文件")
    public ResponseResult deleteThemeStyle(@PathVariable BigInteger id) {
        try {
            return  themeBusiness.removeThemeStyle(id);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }
    @PutMapping("style/{id}")
    @ApiOperation(value = "专题样式文件")
    public ResponseResult deleteThemeStyle(@PathVariable BigInteger id,@RequestBody ThemeStyleEntity themeStyleEntity) {
        try {
            return  themeBusiness.updateThemeStyle(id,themeStyleEntity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }


    @PostMapping("/style/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return ResponseResult.success("成功",themeBusiness.getThemeStyle(searchRequest));
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }


}
