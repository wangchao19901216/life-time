package com.lifetime.security.sms;


import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.manager.service.IUserService;
import com.lifetime.common.redis.constant.RedisConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

public class SmsCodeTokenGranter extends AbstractTokenGranter {


    private RedisTemplate redisTemplate;

    private IUserService userService;

    private static final String SMS_GRANT_TYPE = "sms_code";
    private final AuthenticationManager authenticationManager;

    public SmsCodeTokenGranter(
            AuthenticationManager authenticationManager,
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            RedisTemplate redisTemplate,
            IUserService userService) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, SMS_GRANT_TYPE,
                redisTemplate,userService);

    }

    protected SmsCodeTokenGranter(
            AuthenticationManager authenticationManager,
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            String grantType,
            RedisTemplate redisTemplate,
            IUserService userService) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
        this.redisTemplate=redisTemplate;
        this.userService=userService;
    }


    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client,
                                                           TokenRequest tokenRequest) {

        Map<String, String> parameters =
                new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        // 客户端提交的验证码
        String smsCode = parameters.get("password");
        // 客户端提交的手机号码
        String phoneNumber = parameters.get("username");
        if (StringUtils.isBlank(phoneNumber)||StringUtils.isBlank(smsCode)) {
            throw new RuntimeException(String.valueOf(CommonExceptionEnum.DATA_NOT_EXIST));
        }
        String fwqCode = redisTemplate.opsForValue().get(RedisConstants.MOBILE_LOGIN + phoneNumber)+"";

        if (StringUtils.isBlank(fwqCode)||!smsCode.equals(fwqCode)) {
            throw new RuntimeException(String.valueOf(CommonExceptionEnum.DATA_NOT_EXIST));
        }
        UserEntity userEntity= userService.findByMobile(phoneNumber);

        Authentication userAuth=new SMSCodeAuthenticationToken(userEntity,userEntity.getUsername(),userEntity.getAuthorities());

        userAuth=authenticationManager.authenticate(userAuth);
//        Authentication userAuth =
//                new UsernamePasswordAuthenticationToken(userEntity, null, userEntity.getAuthorities());
        // org.springframework.security.core.userdetails.UserDetails 接口的, 所以有 user.getAuthorities()
        // 当然该参数传null也行
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        OAuth2Request storedOAuth2Request =
                getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }


}
