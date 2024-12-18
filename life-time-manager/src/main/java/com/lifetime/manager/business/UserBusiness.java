package com.lifetime.manager.business;

import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.manager.service.IUserDetailService;
import com.lifetime.common.manager.service.IUserService;
import com.lifetime.common.manager.vo.UserVo;
import com.lifetime.common.model.AuthTokenModel;
import com.lifetime.common.redis.constant.RedisConstants;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.util.CrypToUtil;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.LtModelUtil;
import com.lifetime.manager.config.AuthConfig;
import com.lifetime.manager.model.UserLoginRequestModel;
import com.lifetime.manager.model.UserRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

/**
 * @author:wangchao
 * @date: 2024/12/18-13:51
 * @description: com.lifetime.manager.business
 * @Version:1.0
 */

@Component
public class UserBusiness {
    @Autowired
    IUserDetailService iUserDetailService;
    @Autowired
    IUserService iUserService;

    @Autowired
    AuthConfig authConfig;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RedisTemplate redisTemplate;

    public ResponseResult getToken(String grant_type,UserLoginRequestModel userLoginRequestModel){
        try{
            if(LtCommonUtil.existBlankArgument(userLoginRequestModel.userName,userLoginRequestModel.passWord)){
                return ResponseResult.error(CommonExceptionEnum.ARGUMENT_NULL_EXIST);
            }
            //从Eureka中获取认证服务器的地址
            //从Eureka中获取认证服务的一个实例地址
            //ServiceInstance serviceInstance= loadBalancerClient.choose("");
            //
            //URL url=serviceInstance.getUri();
            //定义head
            LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add("Authorization", getHttpBasic(authConfig.getClientId(), authConfig.getClientSecret()));

            //定义body
            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", grant_type);
            body.add("username", userLoginRequestModel.userName);
            body.add("password", userLoginRequestModel.passWord);
            String authUrl = authConfig.getTokenUrl();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);

            ResponseEntity<AuthTokenModel> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity,
                    AuthTokenModel.class);
            AuthTokenModel authToken = exchange.getBody();
            if (authToken != null) {
                return ResponseResult.success(authToken);
            }
            return ResponseResult.error(CommonExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        catch (Exception exception){
            return  ResponseResult.error(500,exception.getMessage());
        }

    }
    public ResponseResult login(String grant_type,UserLoginRequestModel userLoginRequestModel){
        try{
            ResponseResult responseResult=getToken(grant_type,userLoginRequestModel);
            if(responseResult.getCode()==200){
                AuthTokenModel authTokenModel= (AuthTokenModel) responseResult.getData();
                UserVo userVo= buildUserVo(authTokenModel.getAccess_token(), userLoginRequestModel.userName);
                responseResult.setData(userVo);
            }
            return  responseResult;
        }
        catch (Exception exception){
            return  ResponseResult.error(500,exception.getMessage());
        }
    }
    public ResponseResult loginEncrypt(String grant_type,UserLoginRequestModel userLoginRequestModel){
        try {
            userLoginRequestModel.userName = CrypToUtil.decrypt(userLoginRequestModel.userName);
            userLoginRequestModel.passWord = CrypToUtil.decrypt(userLoginRequestModel.passWord);
            return  login(grant_type,userLoginRequestModel);
        }
        catch (Exception exception){
            return  ResponseResult.error(500,exception.getMessage());
        }
    }

    public ResponseResult loginByMobile(String mobile){
        try {
            UserLoginRequestModel userLoginRequestModel=new UserLoginRequestModel();
            userLoginRequestModel.passWord="123456";
            userLoginRequestModel.userName=mobile;

            redisTemplate.opsForValue().set(RedisConstants.MOBILE_LOGIN + mobile, "123456", 60, TimeUnit.SECONDS);

            return  login("sms_code",userLoginRequestModel);
        }
        catch (Exception exception){
            return  ResponseResult.error(500,exception.getMessage());
        }
    }



    @Transactional()
    public ResponseResult add(UserRequestModel userRequestModel){
        UserDetailEntity userDetail= LtModelUtil.copyTo(userRequestModel,UserDetailEntity.class);
        UserEntity userEntity=new UserEntity();
        userEntity.setUsername(userRequestModel.getUserCode());
        userEntity.setRemark(userRequestModel.getPassWord());
        userEntity.setPassword(passwordEncoder.encode(userRequestModel.getPassWord()));
        userEntity.setMobile(userRequestModel.getMobile());
        iUserService.save(userEntity);
        iUserDetailService.save(userDetail);
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }


    public UserVo buildUserVo(String token,String userCode){
        UserVo userVo=new UserVo();
        userVo.setToken(token);
        userVo.setUserDetail(iUserDetailService.findByUserCode(userCode));
        return  userVo;
    }

    private String getHttpBasic(String clientId, String clientSecret) {
        String string = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }

}
