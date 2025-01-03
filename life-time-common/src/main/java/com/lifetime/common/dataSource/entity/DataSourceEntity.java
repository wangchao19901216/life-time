package com.lifetime.common.dataSource.entity;

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
@TableName("LT_DATA_DATASOURCE")
public class DataSourceEntity extends BaseEntity implements Serializable {
    @ApiModelProperty(name="数据源名称")
    @NotEmpty(message = "数据源名称不为空")
    @TableField("DATA_SOURCE_NAME")
    public String dataSourceName;

    @ApiModelProperty(name="数据源类型",required = true)
    @NotEmpty(message = "数据源类型不为空")
    @TableField("DATA_SOURCE_TYPE")
    public String dataSourceType;
    @ApiModelProperty(name="数据源驱动",required = true)
    @TableField("DRIVER")
    public String driver;
    @ApiModelProperty(name="数据源链接地址",required = true)
    @TableField("DATA_SOURCE_URL")
    public String dataSourceUrl;
    @ApiModelProperty(name="用户")
    @TableField("USER_NAME")
    public String userName;
    @ApiModelProperty(name="密码")
    @TableField("PASSWORD")
    public String passWord;

    @ApiModelProperty(name="扩展字段")
    @TableField("EXTEND")
    public String extent;

    @ApiModelProperty(name="解密密钥")
    @TableField("SECRET_KEY")
    public String secretKey;


}
