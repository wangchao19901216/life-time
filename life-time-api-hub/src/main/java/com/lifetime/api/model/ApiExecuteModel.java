package com.lifetime.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author:wangchao
 * @date: 2025/2/27-15:40
 * @description: com.lifetime.api.model
 * @Version:1.0
 */
@Data
public class ApiExecuteModel implements Serializable {
    String datasourceType;
    String schema;
    String operateType;
    String sql;
    Map<String, Object> params;
}
