package com.lifetime.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseDaoMapper<T> extends BaseMapper<T> {

}
