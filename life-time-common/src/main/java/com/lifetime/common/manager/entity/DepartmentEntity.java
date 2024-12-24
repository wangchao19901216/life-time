package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("LT_M_DEPARTMENT")
public class DepartmentEntity extends BaseEntity {

    @ApiModelProperty(value = "部门编号",notes = "")
    @TableField("DEPARTMENT_CODE")
    private String departmentCode ;

    @ApiModelProperty(value = "部门名称")
    @TableField("DEPARTMENT_NAME")
    private String departmentName;

    @ApiModelProperty(value = "上级部门编号")
    @TableField("DEPARTMENT_PARENT_CODE")
    private String departmentParentCode;

    @ApiModelProperty(value = "部门层级")
    @TableField("DEPARTMENT_LEVEL")
    private Integer departmentLevel;

    @ApiModelProperty(value = "部门分组")
    @TableField("DEPARTMENT_GROUP")
    private String departmentGroup;

    @ApiModelProperty(value = "部门类型(0-目录 1-部门)")
    @TableField("DEPARTMENT_TYPE")
    private String departmentType;

    @ApiModelProperty(value = "部门标签")
    @TableField("DEPARTMENT_TAG")
    private String departmentTag;

    @ApiModelProperty(value = "部门简称")
    @TableField("DEPARTMENT_SHORT_NAME")
    private String departmentShortName;

    @ApiModelProperty(value = "联系方式")
    @TableField("CONTACT_NUMBER")
    private String contactNumber;

    @ApiModelProperty(value = "联系人")
    @TableField("CONTACT_PERSON")
    private String contactPerson;


    @ApiModelProperty(value = "部门标识")
    @TableField("DEPARTMENT_MARK")
    private String departmentMark;
    @ApiModelProperty(value = "部门主页", notes = "")
    @TableField("HOME_URL")
    private String homeUrl;


}
