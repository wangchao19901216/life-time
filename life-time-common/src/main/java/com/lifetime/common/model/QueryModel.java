package com.lifetime.common.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author:wangchao
 * @date: 2023/5/4-19:26
 * @description: com.sh3h.common.util
 * @Version:1.0
 */
@Data
public class QueryModel<T> {
    private Page<T> page;
    private QueryWrapper<T> queryWrapper;
}
