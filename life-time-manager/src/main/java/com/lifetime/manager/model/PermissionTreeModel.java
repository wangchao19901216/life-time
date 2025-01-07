package com.lifetime.manager.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lifetime.common.manager.entity.PermissionEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2025/1/7-13:21
 * @description: com.lifetime.manager.model
 * @Version:1.0
 */
@Data
public class PermissionTreeModel  implements Serializable {
    private String permissionId ;
    private String type;
    private String parentId;
    private String name;
    private String tag;
    private String url;
    private String icon;
    private String openType;
    private String remark;
    private String sort;
    List<PermissionTreeModel> child;
}
