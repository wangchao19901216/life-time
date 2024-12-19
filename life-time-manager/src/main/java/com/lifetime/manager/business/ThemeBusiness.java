package com.lifetime.manager.business;

import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.ThemeConfigEntity;
import com.lifetime.common.manager.entity.ThemeStyleEntity;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.manager.service.IThemeConfigService;
import com.lifetime.common.manager.service.IThemeStyleService;
import com.lifetime.common.manager.service.IUserDetailService;
import com.lifetime.common.manager.service.IUserService;
import com.lifetime.common.manager.vo.UserVo;
import com.lifetime.common.model.AuthTokenModel;
import com.lifetime.common.redis.constant.RedisConstants;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.CrypToUtil;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.LtModelUtil;
import com.lifetime.common.util.PWUtil;
import com.lifetime.manager.config.AuthConfig;
import com.lifetime.manager.model.UserLoginRequestModel;
import com.lifetime.manager.model.UserRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author:wangchao
 * @date: 2024/12/18-13:51
 * @description: com.lifetime.manager.business
 * @Version:1.0
 */

@Component
public class ThemeBusiness {
    @Autowired
    IThemeConfigService iThemeConfigService;

    @Autowired
    IThemeStyleService iThemeStyleService;

    public ResponseResult getThemeConfig() {
        List<ThemeConfigEntity> themeConfigList = iThemeConfigService.getAllThemeConfig();
        Map<String, Object> map = new HashMap<>();
        for (ThemeConfigEntity themeConfig : themeConfigList) {
            map.put(themeConfig.getThemeKey(), themeConfig.getThemeValue());
        }
        return ResponseResult.success(map);
    }

    public ResponseResult updateBatch(Map<String, Object> map) {
        Set<String> keys = map.keySet();
        for (String key : keys) {
            Object value = map.get(key);
            iThemeConfigService.updateByKey(key, (String) value);
        }
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }



    public ResponseResult getThemeStyle(SearchRequest searchRequest){
        return  ResponseResult.success(iThemeStyleService.searchList(searchRequest));
    }

    public ResponseResult removeThemeStyle(BigInteger id){
        return  ResponseResult.success(iThemeStyleService.removeById(id));
    }

    public ResponseResult updateThemeStyle(BigInteger id,ThemeStyleEntity themeStyleEntity){
        themeStyleEntity.setId(id);
        return  ResponseResult.success(iThemeStyleService.updateById(themeStyleEntity));
    }



    public ResponseResult saveThemeStyle(List<ThemeStyleEntity> list){
        return  ResponseResult.success(iThemeStyleService.saveBatch(list));
    }
}
