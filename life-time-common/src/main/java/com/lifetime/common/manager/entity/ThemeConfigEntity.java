package com.lifetime.common.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifetime.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@TableName("LT_T_THEME_CONFIG")
public class ThemeConfigEntity extends BaseEntity {

    @ApiModelProperty(name = "Key值",notes = "")
    @TableField("THEME_KEY")
    public String themeKey ;

    @ApiModelProperty(name = "Value值",notes = "")
    @TableField("THEME_VALUE")
    public String themeValue ;
    @ApiModelProperty(name = "描述",notes = "")
    @TableField("THEME_DES")
    public String themeDes ;

}

