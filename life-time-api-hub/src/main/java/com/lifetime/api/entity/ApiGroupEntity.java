package com.lifetime.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


/**
 * @Auther:wangchao
 * @Date: 2024/12/20-22:45
 * @Description: com.lifetime.common.manager.entity
 * @Version:1.0
 */

@Data
@TableName("lt_api_group")
public class ApiGroupEntity extends BaseEntity {

    @ApiModelProperty(value = "分组编码",notes = "")
    @TableField("GROUP_CODE")
    @NotEmpty(message = "分组编码")
    private String groupCode ;

    @ApiModelProperty(value = "分组名称")
    @TableField("GROUP_NAME")
    private String groupName;

    @ApiModelProperty(value = "上级编码")
    @TableField("PARENT_CODE")
    private String parentCode;

}
