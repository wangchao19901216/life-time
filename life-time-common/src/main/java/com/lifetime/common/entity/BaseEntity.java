package com.lifetime.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    @TableId(type=IdType.ASSIGN_ID)
    @JsonSerialize(using= ToStringSerializer.class)
    public BigInteger id;
    @ApiModelProperty(value = "备注",notes = "")
    public String remark ;
    @ApiModelProperty(value = "排序",notes = "")
    public Integer sort=1 ;
    @ApiModelProperty(value = "状态")
    public Integer status=1;

    @ApiModelProperty(value = "数据类型,1-业务，-1-系统")
    @TableField(value = "data_type")
    public Integer dataType=1;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    public Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "modifier_time",fill = FieldFill.INSERT_UPDATE)
    public  Date modifierTime;

    @TableField(value = "create_user",fill = FieldFill.INSERT)
    public String createUser;

    @TableField(value = "create_user_name",fill = FieldFill.INSERT)
    public String createUserName;

    @TableField(value = "modifier_user",fill = FieldFill.INSERT_UPDATE)
    public String modifierUser;

    @TableField(value = "modifier_user_name",fill = FieldFill.INSERT_UPDATE)
    public String modifierUserName;

    @TableField(value = "opera_depart_code",fill = FieldFill.INSERT)
    public String operaDeptCode;

    @TableField(value = "opera_depart_name",fill = FieldFill.INSERT)
    public String operaDeptName;

}
