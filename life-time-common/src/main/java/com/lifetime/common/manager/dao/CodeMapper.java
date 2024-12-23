package com.lifetime.common.manager.dao;

import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.mapper.BaseDaoMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author:wangchao
 * @date: 2024/12/23-14:08
 * @description: com.lifetime.common.base.dao
 * @Version:1.0
 */
@Mapper
public interface CodeMapper extends BaseDaoMapper<CodeEntity> {

    @Select("select * from LT_M_CODELIST  where CODE_TYPE=#{typeCode} and CODE_VALUE=#{code}")
    CodeEntity isExist(String typeCode,String code);
}
