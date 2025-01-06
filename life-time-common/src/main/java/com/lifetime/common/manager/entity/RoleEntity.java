package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
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

    @ApiModelProperty(value="权限集合")
    @TableField("PERMISSIONS")
    @Lob
    public String permissions;


    @ApiModelProperty(value="权限集合树")
    @TableField("PERMISSION_TREE")
    @Lob
    public String permissionTree;


    @ApiModelProperty(value="数据权限(1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限)")
    @TableField("DATA_SCOPE")
    public String dataScope;
    @ApiModelProperty(value="自定义数据权限(部门)集合")
    @TableField("DATA_DEPTS")
    @Lob
    public String dataDepts;

    @ApiModelProperty(value="自定义数据权限(部门)集合")
    @TableField("DATA_USERS")
    @Lob
    public String dataUsers;



}
