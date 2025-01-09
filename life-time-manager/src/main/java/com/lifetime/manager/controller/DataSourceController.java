package com.lifetime.manager.controller;

import com.lifetime.common.annotation.RepeatSubmit;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.dataSource.entity.DataSourceEntity;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.manager.business.DataSourceBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author:wangchao
 * @date: 2024/12/28-19:47
 * @description: com.lifetime.manager.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("dataSource")
@Api(tags = "数据源")
@PreAuthorize("hasAuthority('all')")
public class DataSourceController {


    @Autowired
    DataSourceBusiness  dataSourceBusiness;
    @PostMapping("")
    @ApiOperation(value = "增加")
    @RepeatSubmit()
    public ResponseResult save(@Validated @RequestBody DataSourceEntity entity) {
        try {
            return  dataSourceBusiness.save(entity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public ResponseResult delete(@PathVariable BigInteger id) {
        try {
            return  dataSourceBusiness.remove(id);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED);
        }
    }
    @PutMapping("/{id}")
    @ApiOperation(value = "修改")
    public ResponseResult update(@PathVariable BigInteger  id,@RequestBody DataSourceEntity entity) {
        try {
            return  dataSourceBusiness.update(id,entity);
        }
        catch (Exception exception){
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED);
        }
    }

    @GetMapping("/connect/{id}")
    @ApiOperation(value = "链接")
    public ResponseResult connect(@PathVariable BigInteger  id) {
        try {
            return  dataSourceBusiness.testConnect(id);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }


    @PostMapping("/connect")
    @ApiOperation(value = "链接")
    public ResponseResult connect(@RequestBody DataSourceEntity  dataSourceEntity) {
        try {
            return  dataSourceBusiness.testConnect(dataSourceEntity);
        }
        catch (Exception exception){
            return ResponseResult.error(500,exception.getMessage());
        }
    }
    @PostMapping("/search")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return  dataSourceBusiness.search(searchRequest);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }



    @PostMapping("/catalog/{id}")
    @ApiOperation(value = "数据库目录")
    public ResponseResult getCatalogs(@PathVariable BigInteger  id) {
        try {
            return  dataSourceBusiness.getCatalogs(String.valueOf(id));
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

    @PostMapping("/schema/{catalog}/{id}")
    @ApiOperation(value = "数据库模式")
    public ResponseResult getSchemas(@PathVariable BigInteger  id,@PathVariable String  catalog) {
        try {
            return  dataSourceBusiness.getSchemas(String.valueOf(id),catalog);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }


    @PostMapping("/table/{schema}/{id}")
    @ApiOperation(value = "数据库表单")
    public ResponseResult getTable(@PathVariable BigInteger  id,@PathVariable String  schema) {
        try {
            return  dataSourceBusiness.getTable(String.valueOf(id),schema);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }

    @PostMapping("/column/{schema}/{table}/{id}")
    @ApiOperation(value = "数据库字段")
    public ResponseResult getColumns(@PathVariable BigInteger  id,@PathVariable String  schema,@PathVariable String  table) {
        try {
            return  dataSourceBusiness.getColumns(String.valueOf(id),schema,table);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.UNHANDLED_EXCEPTION,exception.getMessage());
        }
    }
}
