package com.lifetime.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/23-13:41
 * @description: com.lifetime.common.model
 * @Version:1.0
 */
@Data
public class TreeModel implements Serializable {
    String code;
    String name;
    String type;
    String note;
    List<TreeModel> child;
}

