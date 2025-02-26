package com.lifetime.api.model;

import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiParamEntity;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2025/2/21-11:21
 * @description: com.lifetime.api.model
 * @Version:1.0
 */
@Data
public class ApiModel implements Serializable {
    ApiBaseInfoEntity apiBaseInfo;
    ApiSqlInfoEntity apiSqlInfo;
    List<ApiParamEntity> requestParams;
    List<ApiParamEntity> responseParams;
}
