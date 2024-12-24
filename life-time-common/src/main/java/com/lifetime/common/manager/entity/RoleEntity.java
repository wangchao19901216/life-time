package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;



/**
 * @Auther:wangchao
 * @Date: 2024/12/24-15:20
 * @Description: com.lifetime.common.manager.entity
 * @Version:1.0
 */
@Data
@TableName("LT_M_ROLE")
public class RoleEntity extends BaseEntity implements Serializable {
    @ApiModelProperty(value="角色编号",required = true)
    @TableField("ROLE_CODE")
    public String roleCode;

    @ApiModelProperty(value="角色名称")
    @TableField("ROLE_NAME")
    public String roleName;

    @ApiModelProperty(value="角色类型(0-目录 1-角色)")
    @TableField("ROLE_TYPE")
    public String roleType;

    @ApiModelProperty(value="上级Code,默认为 0000")
    @TableField("ROLE_PARENT_CODE")
    public String roleParentCode="0000";

}
