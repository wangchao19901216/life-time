package com.lifetime.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;

public interface BaseService<T> extends IService<T> {

    //多条件查找
    public SearchResponse<T> searchList(SearchRequest searchRequest);

}
