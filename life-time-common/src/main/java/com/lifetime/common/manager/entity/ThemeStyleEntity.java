package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("LT_T_THEME_STYLE")
public class ThemeStyleEntity extends BaseEntity {

    @ApiModelProperty(name = "Key值",notes = "")
    @TableField("THEME_KEY")
    public String themeKey ;

    @ApiModelProperty(name = "Value值",notes = "")
    @TableField("THEME_VALUE")
    public String themeValue ;
    @ApiModelProperty(name = "描述",notes = "")
    @TableField("THEME_DES")
    public String themeDes ;

    @ApiModelProperty(name = "类型",notes = "")
    @TableField("THEME_TYPE")
    public String themeType ;

    @ApiModelProperty(name = "渐变",notes = "")
    @TableField("THEME_OPACITY")
    public String themeOpacity ;

}

