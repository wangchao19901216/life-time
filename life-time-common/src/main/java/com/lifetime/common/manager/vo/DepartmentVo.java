package com.lifetime.common.manager.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author:wangchao
 * @date: 2024/12/25-19:11
 * @description: com.lifetime.common.manager.vo
 * @Version:1.0
 */
@Data
public class DepartmentVo {
    @ApiModelProperty(value = "部门编号",notes = "")
    private String departmentCode ;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "上级部门编号")
    private String departmentParentCode;

    @ApiModelProperty(value = "部门层级")
    private Integer departmentLevel;

    @ApiModelProperty(value = "部门分组")
    private String departmentGroup;

    @ApiModelProperty(value = "部门类型(0-目录 1-部门)")
    private String departmentType;

    @ApiModelProperty(value = "部门标签")
    private String departmentTag;

    @ApiModelProperty(value = "部门简称")
    private String departmentShortName;

    @ApiModelProperty(value = "联系方式")
    private String contactNumber;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;


    @ApiModelProperty(value = "部门标识")
    private String departmentMark;
    @ApiModelProperty(value = "部门主页", notes = "")
    private String homeUrl;
}
