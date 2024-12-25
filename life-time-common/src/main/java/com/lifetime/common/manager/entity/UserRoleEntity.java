package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

/**
 * @author:wangchao
 * @date: 2024/12/25-10:24
 * @description: com.lifetime.common.manager.entity
 * @Version:1.0
 */
@Data
@TableName("LT_M_USER_ROLE")
@Valid
public class UserRoleEntity extends BaseEntity  {
    @ApiModelProperty(name="用户编码")
    @TableField("USER_CODE")
    public String userCode;

    @ApiModelProperty(name="角色编码")
    @TableField("ROLE_CODE")
    public String roleCode;
}
