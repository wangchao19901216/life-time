package com.lifetime.api.controller;

import com.lifetime.api.business.ApiGroupBusiness;
import com.lifetime.api.business.ApiInfoBusiness;
import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.api.model.ApiModel;
import com.lifetime.common.annotation.RepeatSubmit;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author:wangchao
 * @date: 2025/02/21-14:19
 * @description: com.lifetime.api.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("api/info")
@Api(tags = "接口信息")
@PreAuthorize("hasAuthority('all')")
public class ApiInfoController {
    @Autowired
    ApiInfoBusiness apiInfoBusiness;
    @PostMapping("")
    @ApiOperation(value = "增加")
    public ResponseResult save(@Validated @RequestBody ApiModel model) {
        try {
            return apiInfoBusiness.save(model);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED);
        }
    }


    @DeleteMapping("/{code}")
    @ApiOperation(value = "删除")
    public ResponseResult delete(@PathVariable String code) {
        try {
            return  apiInfoBusiness.remove(code);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }

    @PutMapping("")
    @ApiOperation(value = "修改")
    @RepeatSubmit()
    public ResponseResult update(@RequestBody ApiModel model) {
        try {
            return  apiInfoBusiness.update(model);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }

    @PostMapping("/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return  apiInfoBusiness.search(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }
}
