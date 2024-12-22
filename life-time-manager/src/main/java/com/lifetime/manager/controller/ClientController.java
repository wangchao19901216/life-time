package com.lifetime.manager.controller;

import com.azul.crs.client.service.ClientService;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.ClientEntity;
import com.lifetime.common.manager.service.IClientService;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.util.LtModelUtil;
import com.lifetime.manager.model.ClientRequestModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("client")
@Api(tags = "客户端管理")
@PreAuthorize("hasAuthority('all')")
public class ClientController {

    @Autowired
    private IClientService clientService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("")
    @ApiOperation(value = "增加", notes = "")
    @ApiImplicitParams({})
    public ResponseResult saveOrUpdate(@RequestBody ClientRequestModel clientDto) {
        try {
            clientDto.setClientSecret(passwordEncoder.encode(clientDto.clientSecret));
            clientService.save(LtModelUtil.copyTo(clientDto, ClientEntity.class));
            return ResponseResult.success("成功","成功");
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED,exception.getMessage());
        }
    }


    @PutMapping("")
    @ApiOperation(value = "修改", notes = "")
    @ApiImplicitParams({})
    public ResponseResult update(@RequestBody ClientRequestModel clientDto) {
        try {
            clientService.update(LtModelUtil.copyTo(clientDto, ClientEntity.class));
            return ResponseResult.success("成功","成功");
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED,exception.getMessage());
        }
    }

    @GetMapping("{clientId}")
    @ApiOperation(value = "查询")
    public ResponseResult findById(@PathVariable String clientId) {

        try {
            return ResponseResult.success("成功",clientService.findByClientId(clientId));
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.ERROR_EMPTY,exception.getMessage());
        }
    }

    @PostMapping("/searchList")
    @ApiOperation(value = "查询")
    public ResponseResult searchList(@RequestBody SearchRequest searchRequest) {
        try {
            return ResponseResult.success("成功",clientService.searchList(searchRequest));
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_SEARCH_FAILED,exception.getMessage());
        }

    }
    @DeleteMapping("{clientId}")
    @ApiOperation(value = "删除")
    public ResponseResult delete(@PathVariable String clientId) {

        try {
            clientService.deleteByClientId(clientId);
            return ResponseResult.success("成功","成功");
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED,exception.getMessage());
        }
    }



    @GetMapping("password/{password}/{clientId}")
    @ApiOperation(value = "修改", notes = "")
    @ApiImplicitParams({})
    public ResponseResult updatePassword(@PathVariable("password") String password,@PathVariable("clientId") String clientId) {
        try {
            ClientEntity clientEntity= clientService.findByClientId(clientId);
            clientEntity.setClientSecret(passwordEncoder.encode(password));
            clientService.update(clientEntity);
            return ResponseResult.success("成功","成功");
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED,exception.getMessage());
        }
    }

}
