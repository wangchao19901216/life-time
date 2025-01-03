package com.lifetime.manager.business;

import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.util.json.JsonUtil;
import com.lifetime.common.manager.entity.*;
import com.lifetime.common.manager.service.*;
import com.lifetime.common.manager.vo.DepartmentVo;
import com.lifetime.common.manager.vo.UserTestVo;
import com.lifetime.common.manager.vo.UserVo;
import com.lifetime.common.model.AuthTokenModel;
import com.lifetime.common.redis.constant.RedisConstants;
import com.lifetime.common.redis.util.RedisUtil;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.util.*;
import com.lifetime.manager.config.AuthConfig;
import com.lifetime.manager.model.UserLoginRequestModel;
import com.lifetime.manager.model.UserRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    IUserRoleService iUserRoleService;

    @Autowired
    IUserDepartmentService iUserDepartmentService;

    @Autowired
    IDepartmentService iDepartmentService;

    @Autowired
    AuthConfig authConfig;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RedisUtil redisUtil;

    public AuthTokenModel getToken(String grant_type, UserLoginRequestModel userLoginRequestModel) {
        if (LtCommonUtil.existBlankArgument(userLoginRequestModel.userCode, userLoginRequestModel.passWord)) {

            throw new RuntimeException(CommonExceptionEnum.ARGUMENT_NULL_EXIST.getMessage());
        }
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", getHttpBasic(authConfig.getClientId(), authConfig.getClientSecret()));

        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grant_type);
        body.add("username", userLoginRequestModel.userCode);
        body.add("password", userLoginRequestModel.passWord);
        String authUrl = authConfig.getTokenUrl();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);

        ResponseEntity<AuthTokenModel> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity,
                AuthTokenModel.class);
        return exchange.getBody();
    }

    public AuthTokenModel refreshToken(String refreshToken) {
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", getHttpBasic(authConfig.getClientId(), authConfig.getClientSecret()));

        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        String authUrl = authConfig.getTokenUrl();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);
        ResponseEntity<AuthTokenModel> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity,
                AuthTokenModel.class);
        return exchange.getBody();
    }


    public UserVo loginWithRedis(String grant_type, UserLoginRequestModel userLoginRequestModel) {
        String LOGIN_REDIS_KEY = RedisConstants.LOGIN_USER + grant_type.toUpperCase() + ":" + userLoginRequestModel.userCode;
        AuthTokenModel authTokenModel = getToken(grant_type, userLoginRequestModel);
        UserVo userVo = buildUserVo(authTokenModel, userLoginRequestModel.userCode);
        redisUtil.set(LOGIN_REDIS_KEY, userVo, Integer.valueOf(authTokenModel.expires_in));
        return userVo;
    }

    public UserVo refreshTokenWithRedis(String grant_type, UserLoginRequestModel userLoginRequestModel) {
        try {
            String LOGIN_REDIS_KEY = RedisConstants.LOGIN_USER + grant_type.toUpperCase() + ":" + userLoginRequestModel.userCode;
            //转换有点问题
            UserVo userVo = LtModelUtil.copyTo(redisUtil.get(LOGIN_REDIS_KEY), UserVo.class);
            AuthTokenModel authTokenModel = refreshToken(userVo.getAuthToken().getRefresh_token());
            userVo.setAuthToken(authTokenModel);
            redisUtil.set(LOGIN_REDIS_KEY, userVo, Integer.valueOf(authTokenModel.expires_in));
            return userVo;
        }catch (Exception exception){
            throw  new RuntimeException("refresh_token 过期");
        }

    }

    public ResponseResult login(String grant_type, UserLoginRequestModel userLoginRequestModel) {
        try {
            AuthTokenModel authTokenModel = getToken(grant_type, userLoginRequestModel);
            UserVo userVo = buildUserVo(authTokenModel, userLoginRequestModel.userCode);
            return ResponseResult.success(userVo);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }
    public ResponseResult loginDependOnRedis(String grant_type, UserLoginRequestModel userLoginRequestModel) {
        try {
            return ResponseResult.success(loginWithRedis(grant_type,userLoginRequestModel));
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }
    public ResponseResult refreshDependOnRedis(String refreshToken) {
        try {
            return ResponseResult.success(refreshToken(refreshToken));
        } catch (Exception exception) {
            return ResponseResult.error(500,exception.getMessage());
        }
    }

    public ResponseResult getUserDependOnRedis(String grant_type, UserLoginRequestModel userLoginRequestModel) {
        UserVo userVo=null;
        try {
            String LOGIN_REDIS_KEY = RedisConstants.LOGIN_USER + grant_type.toUpperCase() + ":" + userLoginRequestModel.userCode;

            if(LtCommonUtil.isNotBlankOrNull(redisUtil.get(LOGIN_REDIS_KEY))){
                userVo=refreshTokenWithRedis(grant_type,userLoginRequestModel);
            }
            else{
                userVo=loginWithRedis(grant_type,userLoginRequestModel);
            }
            return ResponseResult.success(userVo);
        } catch (Exception exception) {
            if(exception.getMessage().equals("refresh_token 过期")){
                userVo=loginWithRedis(grant_type,userLoginRequestModel);
                userVo.setActiveDept(exception.getMessage());
                return ResponseResult.success(userVo);
            }
            return ResponseResult.error(CommonExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }

    public ResponseResult loginEncrypt(String grant_type, UserLoginRequestModel userLoginRequestModel) {
        try {
            userLoginRequestModel.userCode = CrypToUtil.decrypt(userLoginRequestModel.userCode);
            userLoginRequestModel.passWord = CrypToUtil.decrypt(userLoginRequestModel.passWord);
            return login(grant_type, userLoginRequestModel);
        } catch (Exception exception) {
            return ResponseResult.error(500, exception.getMessage());
        }
    }

    public ResponseResult loginByMobile(String mobile) {
        try {
            UserLoginRequestModel userLoginRequestModel = new UserLoginRequestModel();
            userLoginRequestModel.passWord = "123456";
            userLoginRequestModel.userCode = mobile;
            redisUtil.set(RedisConstants.MOBILE_LOGIN + mobile, "123456", 60);

            return login("sms_code", userLoginRequestModel);
        } catch (Exception exception) {
            return ResponseResult.error(500, exception.getMessage());
        }
    }

    @Transactional
    public ResponseResult add(String deptCode,UserRequestModel userRequestModel){
        UserEntity resUser = iUserService.findByUserCode(userRequestModel.userCode);
        if (LtCommonUtil.isBlankOrNull(resUser)) {
            UserDetailEntity userDetail = LtModelUtil.copyTo(userRequestModel, UserDetailEntity.class);
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userRequestModel.getUserCode());
            userEntity.setRemark(userRequestModel.getPassWord());
            userEntity.setPassword(passwordEncoder.encode(userRequestModel.getPassWord()));
            userEntity.setMobile(userRequestModel.getMobile());
            iUserService.save(userEntity);
            iUserDetailService.save(userDetail);
            if(LtCommonUtil.isNotBlankOrNull(deptCode)){
                UserDepartmentEntity userDepartmentEntity=new UserDepartmentEntity();
                userDepartmentEntity.setUserCode(userRequestModel.getUserCode());
                userDepartmentEntity.setDepartmentCode(deptCode);
                userDepartmentEntity.setActiveDept(deptCode);
                iUserDepartmentService.save(userDepartmentEntity);
            }
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        } else {
            return ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED.getCode(), CommonExceptionEnum.DATA_SAVE_FAILED.getMessage(), "用户已经存在");
        }
    }

    @Transactional
    public ResponseResult update(String userCode, UserDetailEntity mUserDetailEntity) {
        UserDetailEntity resultEntity = iUserDetailService.findByUserCode(userCode);
        mUserDetailEntity.setId(resultEntity.getId());
        iUserDetailService.updateById(mUserDetailEntity);

        UserEntity userEntity = iUserService.findByUserCode(userCode);
        userEntity.setStatus(mUserDetailEntity.status);
        iUserService.updateById(userEntity);
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }

    @Transactional
    public ResponseResult delete(String userCode) {
        UserDetailEntity resultEntity = iUserDetailService.findByUserCode(userCode);
        iUserDetailService.removeById(resultEntity.getId());
        UserEntity userEntity = iUserService.findByUserCode(userCode);
        iUserService.removeById(userEntity.getId());
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }

    public ResponseResult updatePassWord(UserLoginRequestModel userLoginRequestModel) {
        iUserService.updatePassword(userLoginRequestModel.userCode, passwordEncoder.encode(userLoginRequestModel.passWord));
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }

    public ResponseResult checkPassWord(UserLoginRequestModel userLoginRequestModel) {
        if (iUserService.checkPassword(userLoginRequestModel.userCode, userLoginRequestModel.passWord))
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        else
            return ResponseResult.error(CommonExceptionEnum.ARGUMENT_ERROR);
    }

    public ResponseResult searchPWLevel(UserLoginRequestModel userLoginRequestModel) {
        return ResponseResult.success(PWUtil.getPasswordLevel(userLoginRequestModel.getPassWord()).getMessage());
    }


    public UserVo buildUserVo(AuthTokenModel authTokenModel, String userCode) {
        List<String> deptCodeList=iUserDepartmentService.findByUserCode(userCode).stream().map(UserDepartmentEntity::getDepartmentCode).collect(Collectors.toList());
        List<DepartmentVo> departmentEntityList=new ArrayList<>();
        if(deptCodeList.size()>0){
            departmentEntityList=LtModelUtil.copyCollectionTo(iDepartmentService.findByDeptCodeArray(deptCodeList.toArray(new String[0])),DepartmentVo.class) ;
        }
        return UserVo.builder()
                .userDetail(iUserDetailService.findByUserCode(userCode))
                .authToken(authTokenModel)
                .departments(departmentEntityList)
                .activeDept(departmentEntityList.size()>0?departmentEntityList.get(0).getDepartmentCode():null)
                .build();
    }

    private String getHttpBasic(String clientId, String clientSecret) {
        String string = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }


    @Transactional
    public ResponseResult bindUserRole(List<UserRoleEntity> requestList) {
        List<UserRoleEntity> list = new ArrayList<>();
        String userCode=requestList.get(0).getUserCode();
        String activeDept=requestList.get(0).getRoleDept();

        //库里原始数据
        List<UserRoleEntity> originalList = iUserRoleService.findByUserCode(userCode, activeDept);
        for (UserRoleEntity userRoleEntity : requestList) {
            if (LtCommonUtil.isNotBlankOrNull(userRoleEntity.getId())) {
                userRoleEntity.setDepartCode(activeDept);
                list.add(userRoleEntity);
            } else {
                UserRoleEntity entity = LtModelUtil.copyTo(userRoleEntity, UserRoleEntity.class);
                entity.setId(BigInteger.valueOf(SnowflakeUtil.nextLongId()));
                entity.setDepartCode(activeDept);
                list.add(entity);
            }
        }
        iUserRoleService.saveOrUpdateBatch(list);

        /**
         * 处理删除的数据
         * */
        //过滤掉新增的角色
        List<UserRoleEntity> sourceList = requestList.stream().filter(e -> LtCommonUtil.isNotBlankOrNull(e.getId())).collect(Collectors.toList());

        //已经删除的角色
        List<UserRoleEntity> deleteList = originalList.stream()
                .filter(p ->
                        sourceList.stream().filter(
                                r -> Objects.equals(r.getId(), p.getId())).collect(Collectors.toList()).size() == 0
                ).collect(Collectors.toList());

        if (deleteList.size() > 0) {
            iUserRoleService.removeBatchByIds(deleteList);
        }
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }
    public ResponseResult saveUserRole(List<UserRoleEntity> requestList) {
        return ResponseResult.success(iUserRoleService.saveOrUpdateBatch(requestList));
    }
    public ResponseResult removeUserRole(BigInteger id) {
        return ResponseResult.success(iUserRoleService.removeById(id));
    }


    public ResponseResult searchUserRole(String userCode,String activeDept) {
        //库里原始数据
        List<UserRoleEntity> originalList = iUserRoleService.findByUserCode(userCode, activeDept);
        return ResponseResult.success(originalList);
    }




    @Transactional
    public ResponseResult bindUserDept(List<UserDepartmentEntity> requestList) {
        List<UserDepartmentEntity> list = new ArrayList<>();
        String userCode=requestList.get(0).getUserCode();
        //库里原始数据
        List<UserDepartmentEntity> originalList = iUserDepartmentService.findByUserCode(userCode);
        for (UserDepartmentEntity userDepartmentEntity : requestList) {
            if (LtCommonUtil.isNotBlankOrNull(userDepartmentEntity.getId())) {
                list.add(userDepartmentEntity);
            } else {
                UserDepartmentEntity entity = LtModelUtil.copyTo(userDepartmentEntity, UserDepartmentEntity.class);
                entity.setId(BigInteger.valueOf(SnowflakeUtil.nextLongId()));
                list.add(entity);
            }
        }
        iUserDepartmentService.saveOrUpdateBatch(list);

        /**
         * 处理删除的按钮权限
         * */
        //过滤掉新增的数据
        List<UserDepartmentEntity> sourceList = requestList.stream().filter(e -> LtCommonUtil.isNotBlankOrNull(e.getId())).collect(Collectors.toList());

        //已经删除的数据
        List<UserDepartmentEntity> deleteList = originalList.stream().filter(p -> sourceList.stream().filter(r -> Objects.equals(r.getId(), p.getId())).collect(Collectors.toList()).size() == 0).collect(Collectors.toList());

        if (deleteList.size() > 0) {
            iUserDepartmentService.removeBatchByIds(deleteList);
        }
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }

    public ResponseResult saveUserDept(List<UserDepartmentEntity> requestList) {
        return ResponseResult.success(iUserDepartmentService.saveOrUpdateBatch(requestList));
    }
    public ResponseResult removeUserDept(BigInteger id) {
        return ResponseResult.success(iUserDepartmentService.removeById(id));
    }
    public ResponseResult searchUserDept(String userCode) {
        //库里原始数据
        List<UserDepartmentEntity> originalList = iUserDepartmentService.findByUserCode(userCode);
        return ResponseResult.success(originalList);
    }


    public ResponseResult search(SearchRequest searchRequest) {
        return ResponseResult.success(iUserDetailService.searchList(searchRequest));
    }
    public ResponseResult searchByDepart(SearchRequest searchRequest,String deptCode) {
        return ResponseResult.success(iUserDetailService.findByDept(searchRequest,deptCode));
    }


    public String getActiveDept(String userCode){
        String activeDept="";
        List<UserDepartmentEntity> userDepartmentEntityList=iUserDepartmentService.findByUserCode(userCode);
        if(userDepartmentEntityList.size()>0){
            for (UserDepartmentEntity entity:userDepartmentEntityList){
                if(entity.activeDept.equals("1")){
                    activeDept= entity.getDepartmentCode();
                    break;
                }
            }
            //防止初始值为0
            if(activeDept.equals("")){
                activeDept=userDepartmentEntityList.get(0).getDepartmentCode();
            }
        }
        return activeDept;
    }

}
