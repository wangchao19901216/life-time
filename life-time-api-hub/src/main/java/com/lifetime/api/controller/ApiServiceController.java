package com.lifetime.api.controller;

import com.lifetime.common.response.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:wangchao
 * @date: 2025/2/25-09:46
 * @description: com.lifetime.api.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("api/common")
@Api(tags = "通用接口服务")
public class ApiServiceController {
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseResult getService() {
        return ResponseResult.success("success");
    }
}
