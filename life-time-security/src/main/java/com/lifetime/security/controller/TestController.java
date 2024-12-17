package com.lifetime.security.controller;

import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.manager.service.IUserService;
import com.lifetime.common.redis.util.RedisUtil;
import com.lifetime.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:wangchao
 * @date: 2024/12/16-16:33
 * @description: com.lifetime.manager.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("test")
@Api(tags = "测试")
public class TestController {
    @Autowired
    IUserService userService;
    @Autowired
    RedisUtil redisUtil;
    @GetMapping("")
    @ApiOperation(value = "查询")
    public ResponseResult findById() {
        try {
            redisUtil.set("22","11122");
          UserEntity userEntity= userService.findByUserCode("admin");
            return ResponseResult.success("成功",userEntity);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.ERROR_EMPTY,exception.getMessage());
        }
    }
}
