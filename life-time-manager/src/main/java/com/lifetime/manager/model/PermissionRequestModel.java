package com.lifetime.manager.model;

import com.lifetime.common.manager.entity.PermissionEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/20-15:21
 * @description: com.lifetime.manager.model
 * @Version:1.0
 */
@Builder
@Data
public class PermissionRequestModel {
    PermissionEntity permission;
    List<PermissionEntity> buttons;
}
