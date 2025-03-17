package com.lifetime.api.model;

import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiParamEntity;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.api.entity.AppEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2025/2/26-16:12
 * @description: com.lifetime.api.model
 * @Version:1.0
 */
@Data
public class ApiCacheModel  implements Serializable {
    /**
     * 请求时间
     */
    private Date requestDate;

    private Long requestTime;

    private String userId;

    ApiModel apiInfo;
    List<AppEntity>  apps;

}
