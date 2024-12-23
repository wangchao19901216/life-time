package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Auther:wangchao
 * @Date: 2022/6/26-14:52
 * @Description: com.sh3h.base.entity
 * @Version:1.0
 */
@Data
@TableName("LT_M_CODELIST")
public class CodeEntity extends BaseEntity implements Serializable {
    @ApiModelProperty(name="分组")
    @NotEmpty(message = "字典分组不为空")
    @TableField("CODE_TYPE")
    public String codeType;

    @ApiModelProperty(name="字典编号",required = true)
    @NotEmpty(message = "字典编号不为空")
    @TableField("CODE_VALUE")
    public String codeValue;
    @ApiModelProperty(name="字典名称",required = true)
    @NotEmpty(message = "字典名称不为空")
    @TableField("CODE_NAME")
    public String codeName;
    @ApiModelProperty(name="字典归属，0为父节点",required = true)
    @NotEmpty(message = "字典归属，0为父节点")
    @TableField("CODE_PARENT")
    public String codeParent;
    @ApiModelProperty(name="字典备注")
    @TableField("CODE_NOTE")
    public String codeNote;
    @ApiModelProperty(name="展示样式")
    @TableField("CODE_CLASS")
    public String codeClass;

    @ApiModelProperty(name="参数一")
    @TableField("FIRST_PARAM")
    public String firstParam;

    @ApiModelProperty(name="参数二")
    @TableField("SECOND_PARAM")
    public String secondParam;

    @ApiModelProperty(name="参数三")
    @TableField("THIRD_PARAM")
    public String thirdParam;

    @ApiModelProperty(name="参数四")
    @TableField("FOURTH_PARAM")
    public String fourthParam;


}
