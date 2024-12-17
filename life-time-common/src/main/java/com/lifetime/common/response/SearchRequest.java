package com.lifetime.common.response;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2023/5/4-18:59
 * @description: com.sh3h.common.response
 * @Version:1.0
 */
public class SearchRequest {
    //分页信息
    public PageModel pageParams;
    //查询条件
    public List<SearchModel> searchParams;

    public List<SearchModel> otherParams;


}
