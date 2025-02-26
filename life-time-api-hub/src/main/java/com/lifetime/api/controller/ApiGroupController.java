package com.lifetime.api.controller;

import com.lifetime.api.business.ApiGroupBusiness;
import com.lifetime.api.entity.ApiGroupEntity;
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
 * @date: 2024/12/18-14:19
 * @description: com.lifetime.manager.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("api/group")
@Api(tags = "接口分组")
@PreAuthorize("hasAuthority('all')")
public class ApiGroupController {
    @Autowired
    ApiGroupBusiness apiGroupBusiness;
    @PostMapping("")
    @ApiOperation(value = "增加")
    public ResponseResult save(@Validated @RequestBody ApiGroupEntity entity) {
        try {
            return apiGroupBusiness.save(entity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public ResponseResult delete(@PathVariable BigInteger id) {
        try {
            return  apiGroupBusiness.remove(id);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }
    @PutMapping("/{id}")
    @ApiOperation(value = "修改")
    @RepeatSubmit()
    public ResponseResult update(@PathVariable BigInteger  id,@RequestBody ApiGroupEntity entity) {
        try {
            return  apiGroupBusiness.update(id,entity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }

    @PostMapping("/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return  apiGroupBusiness.search(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

    @PostMapping("/tree")
    @ApiOperation(value = "查询-树型")
    public ResponseResult tree(@RequestBody SearchRequest searchRequest) {
        try {
            return  apiGroupBusiness.getTree(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

}
