package com.lifetime.common.manager.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@TableName("LT_M_PERMISSION")
public class PermissionEntity extends BaseEntity {

    @ApiModelProperty(name = "权限编号",notes = "")
    @TableField("PERMISSION_ID")
    @NotEmpty(message = "权限编号不能为空")
    private String permissionId ;

    @ApiModelProperty(value = "权限类型(0-目录 1-菜单 2-按钮)")
    @TableField("TYPE")
    private String type;

    @ApiModelProperty(value = "权限父节点")
    @TableField("PARENT_ID")
    @NotEmpty(message = "权限父节点不能为空")
    private String parentId;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限标识")
    private String tag;

    @ApiModelProperty(value = "路径")
    private String url;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "打开方式(0-route 1-iframe 2-dialog 3-blank)")
    @TableField("OPEN_TYPE")
    private String openType;
}
