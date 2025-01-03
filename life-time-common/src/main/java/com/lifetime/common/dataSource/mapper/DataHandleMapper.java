package com.lifetime.common.dataSource.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface DataHandleMapper {
    /**
     * 查询类SQL
     * @param params
     * @return
     */
    @SelectProvider(type = LtSelectProvider.class, method = "executeQuery")
    List<Map<String, Object>> executeQuery(Map<String, Object> params);


    @SelectProvider(type = LtSelectProvider.class, method = "executeQuery_Page")
    IPage<Object> executeQuery_Page(Page<Object> page, Map<String, Object> params);
    /**
     * 新增类SQL
     * @param params
     * @return
     */
    @InsertProvider(type = LtSelectProvider.class, method = "executeInsert")
    Integer executeInsert(Map<String, Object> params);


    @Select("")
    IPage<Object> Select();




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

