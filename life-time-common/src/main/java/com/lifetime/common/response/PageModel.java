package com.lifetime.common.response;

import lombok.Data;

/**
 * @author:wangchao
 * @date: 2023/5/4-19:08
 * @Description: sh3h.message.application.response
 * @Version:1.0
 */
@Data
public class PageModel {
    public int total = 0;
    public int pageSize = 10;
    public int pageIndex=0;
}
