package com.lifetime.common.dataSource.spi;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/28-21:43
 * @description: 基础操作
 * @Version:1.0
 */
public interface IStatementMapper<T> extends LtMapper<T>{

    /**
     * 查询单个对象
     *
     * @param dataSourceId
     * @param schema
     * @param sql
     * @param params
     * @return
     */
    T selectOne(String dataSourceId, String schema, String sql, Object params);


    /**
     * 查询对象列表
     *
     * @param dataSourceId
     * @param schema
     * @param sql
     * @param params
     * @return
     */
    List<T> selectList(String dataSourceId, String schema, String sql, Object params);


    /**
     * 查询分页对象
     *
     * @param dataSourceId
     * @param schema
     * @param sql
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Object> selectPage(String dataSourceId, String schema, String sql, Object params, int pageNum, int pageSize);

    /**
     * 新增
     *
     * @param dataSourceId
     * @param schema
     * @param sql
     * @param params
     * @return
     */
    int insert(String dataSourceId, String schema, String sql, Object params);

    /**
     * 删除
     *
     * @param dataSourceId
     * @param schema
     * @param sql
     * @param params
     * @return
     */
    int delete(String dataSourceId, String schema, String sql, Object params);

    /**
     * 修改
     *
     * @param dataSourceId
     * @param schema
     * @param sql
     * @param params
     * @return
     */
    int update(String dataSourceId, String schema, String sql, Object params);
}
