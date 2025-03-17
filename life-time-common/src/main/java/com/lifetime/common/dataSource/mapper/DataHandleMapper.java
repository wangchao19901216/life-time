package com.lifetime.common.dataSource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifetime.common.manager.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author:wangchao
 * @date: 2024/12/30-19:17
 * @description: com.lifetime.common.dataSource.mapper
 * @Version:1.0
 */
@Mapper
public interface DataHandleMapper extends BaseMapper<UserEntity> {
    /**
     * 查询类SQL
     * @param params
     * @return
     */
    @SelectProvider(type = LtSelectProvider.class, method = "executeQuery")
    List<Map<String, Object>> executeQuery(Map<String, Object> params);


    @SelectProvider(type = LtSelectProvider.class, method = "executeQuery_Page")
    IPage<Map<String, Object>> executeQuery_Page(Page<Map<String, Object>> page,@Param("params") Map<String, Object> params);
    /**
     * 新增类SQL
     * @param params
     * @return
     */
    @InsertProvider(type = LtSelectProvider.class, method = "executeInsert")
    Integer executeInsert(Map<String, Object> params);

    /**
     * 修改类SQL
     * @param params
     * @return
     */
    @UpdateProvider(type = LtSelectProvider.class, method = "executeUpdate")
    Integer executeUpdate(Map<String, Object> params);

    /**
     * 删除类SQL
     * @param params
     * @return
     */
    @UpdateProvider(type = LtSelectProvider.class, method = "executeDelete")
    Integer executeDelete(Map<String, Object> params);
}

