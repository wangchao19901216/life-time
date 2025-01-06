package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author:wangchao
 * @date: 2024/12/25-10:24
 * @description: com.lifetime.common.manager.entity
 * @Version:1.0
 */
@Data
@TableName("LT_M_USER_DEPARTMENT")
public class UserDepartmentEntity extends BaseEntity  {
    @ApiModelProperty(name="用户编码")
    @TableField("USER_CODE")
    @NotEmpty(message="用户编码不能为空")
    public String userCode;

    @ApiModelProperty(name="部门编码")
    @TableField("DEPARTMENT_CODE")
    @NotEmpty(message="部门编码不能为空")
    public String departmentCode;

    @ApiModelProperty(name="部门名称")
    @TableField("DEPARTMENT_NAME")
    public transient String departmentName;


    @ApiModelProperty(name="是否当前用户部门(用户多部门切换)")
    @TableField("ACTIVE_DEPARTMENT")
    public String activeDept="1";
}
