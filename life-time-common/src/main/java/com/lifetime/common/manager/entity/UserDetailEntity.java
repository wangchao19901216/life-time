package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.annotation.PrivacyEncrypt;
import com.lifetime.common.entity.BaseEntity;
import com.lifetime.common.enums.PrivacyTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@TableName("LT_M_USER_DETAIL")
public class UserDetailEntity extends BaseEntity  {

    @ApiModelProperty(name = "用户编码",notes = "")
    @TableField("USER_CODE")
    @NotEmpty(message = "用户编码不能为空")
    public String userCode ;

    @ApiModelProperty(name = "用户名称",notes = "")
    @TableField("USER_NAME")
    public String userName ;

    @ApiModelProperty(name = "用户昵称",notes = "")
    @TableField("USER_NICK_NAME")
    public String userNickName ;

    @ApiModelProperty(name = "手机号",notes = "")
    public String mobile ;

    @ApiModelProperty(name = "头像",notes = "")
    @TableField("AVATAR")
    public String avatar ;
    @PrivacyEncrypt(type = PrivacyTypeEnum.ID_CARD)
    @ApiModelProperty(name = "身份证号",notes = "")
    public String idCard ;

    @ApiModelProperty(name = "性别",notes = "")
    public Integer sex ;

    @ApiModelProperty(name = "邮箱",notes = "")
    public String email ;

    @ApiModelProperty(name = "微信号",notes = "")
    public String wx ;

    @ApiModelProperty(name = "家庭住址",notes = "")
    public String address ;
}

