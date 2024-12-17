package com.lifetime.common.response;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2023/5/4-19:08
 * @Description: 请求返回的统一格式
 * @Version:1.0
 */
public class SearchResponse<T> {
    public PageModel pageInfo;
    public List<T>  results;
}
