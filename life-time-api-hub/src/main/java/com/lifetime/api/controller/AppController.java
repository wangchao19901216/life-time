package com.lifetime.api.controller;

import com.lifetime.api.business.ApiInfoBusiness;
import com.lifetime.api.business.AppBusiness;
import com.lifetime.api.entity.AppEntity;
import com.lifetime.api.model.ApiModel;
import com.lifetime.common.annotation.RepeatSubmit;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author:wangchao
 * @date: 2025/2/25-09:46
 * @description: com.lifetime.api.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("app")
@Api(tags = "应用管理")
public class AppController {
    @Autowired
    AppBusiness appBusiness;
    @PostMapping("")
    @ApiOperation(value = "增加")
    public ResponseResult save(@Validated @RequestBody AppEntity model) {
        try {
            return appBusiness.save(model);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED);
        }
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public ResponseResult delete(@PathVariable BigInteger code) {
        try {
            return  appBusiness.remove(code);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }

    @PutMapping("")
    @ApiOperation(value = "修改")
    @RepeatSubmit()
    public ResponseResult update(@RequestBody AppEntity model) {
        try {
            return  appBusiness.update(model);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }

    @PostMapping("/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return  appBusiness.search(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }
}
