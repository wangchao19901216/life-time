package com.lifetime.common.dataSource.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifetime.common.constant.DataSourceConstants;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author:wangchao
 * @date: 2024/12/30-19:14
 * @description:  mybatis SQL提供器
 * @Version:1.0
 */

@Mapper
public class LtSelectProvider {

    private static final String[] tags = {"</if>","</foreach>","</where>","</set>","</choose>","</when>","</trim>","</otherwise"};
    /**
     * 添加脚本标签前缀
     * @param sql
     * @return
     */
    private String addScript(String sql){
        for(String tag : tags) {
            if (sql.contains(tag)) {
                sql = "<script> " + sql + " </script>";
                break;
            }
        }
        return sql;
    }

    /**
     * 查询类
     * @param params
     * @return
     */
    public String executeQuery(Map<String,Object> params) {
        String sql = params.get(DataSourceConstants.BASE_SQL).toString();
        params.remove(DataSourceConstants.BASE_SQL);
        sql = this.addScript(sql);
        return sql;
    }


    /**
     * 查询类
     * @param params
     * @return
     */
    public String executeQuery_Page(Page<Object> page, Map<String,Object> params) {
        String sql = params.get(DataSourceConstants.BASE_SQL).toString();
        params.remove(DataSourceConstants.BASE_SQL);
        sql = this.addScript(sql);
        return sql;
    }


    /**
     * 新增类
     * @param params
     * @return
     */
    public String executeInsert(Map<String,Object> params) {
        String sql = params.get(DataSourceConstants.BASE_SQL).toString();
        params.remove(DataSourceConstants.BASE_SQL);
        sql = this.addScript(sql);
        return sql;
    }

    /**
     * 修改类
     * @param params
     * @return
     */
    public String executeUpdate(Map<String,Object> params) {
        String sql = params.get(DataSourceConstants.BASE_SQL).toString();
        params.remove(DataSourceConstants.BASE_SQL);
        sql = this.addScript(sql);
        return sql;
    }

    /**
     * 删除类
     * @param params
     * @return
     */
    public String executeDelete(Map<String,Object> params) {
        String sql = params.get(DataSourceConstants.BASE_SQL).toString();
        params.remove(DataSourceConstants.BASE_SQL);
        sql = this.addScript(sql);
        return sql;
    }
}
