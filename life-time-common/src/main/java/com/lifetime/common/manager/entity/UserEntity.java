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
@TableName("LT_USER")
public class UserEntity extends BaseEntity implements UserDetails {

    @ApiModelProperty(name = "用户编码",notes = "")
    @TableField("USER_CODE")
    public String username ;

    @ApiModelProperty(name = "用户密码",notes = "")
    public String password ;
    @ApiModelProperty(name = "手机号",notes = "")
    public String mobile ;

    @ApiModelProperty(name = "帐户是否可用(1 可用，0 删除用户)",notes = "",hidden = true)
    @TableField("is_enabled")
    public boolean isEnabled=true ;

    @ApiModelProperty(name = "密码是否过期(1 未过期，0已过期)",notes = "",hidden = true)
    @TableField("is_credentials_non_expired")
    public boolean isCredentialsNonExpired=true ;

    @ApiModelProperty(name = "帐户是否被锁定(1 未过期，0已过期)",notes = "",hidden = true)
    @TableField("is_account_non_locked")
    public boolean isAccountNonLocked=true ;

    @ApiModelProperty(name = "帐户是否过期(1 未过期，0已过期)",notes = "",hidden = true)
    @TableField("is_account_non_expired")
    public boolean isAccountNonExpired=true ;

    public transient String token;

    public transient String authorities="all";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(StringUtils.isEmpty(authorities)){
            authorities="all";
        }
        return Arrays.stream(authorities.split(",")).map(e->new SimpleGrantedAuthority(e)).collect(Collectors.toSet());
    }
}

