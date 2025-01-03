package com.lifetime.common.dataSource.driver.jdbc;

import com.lifetime.common.constant.DataSourceConstants;
import com.lifetime.common.dataSource.config.JdbcDataSourceRouter;
import com.lifetime.common.dataSource.mapper.DataHandleMapper;
import com.lifetime.common.dataSource.spi.IStatementMapper;
import com.lifetime.common.enums.ApiStatusEnum;
import com.lifetime.common.exception.CommonException;
import org.springframework.stereotype.Component;
//import com.lifetime.common.response.ApiPageInfo;
//import com.lifetime.common.response.PageModel;
//import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author:wangchao
 * @date: 2024/12/29-11:14
 * @description: com.lifetime.common.dataSource.driver.jdbc
 * @Version:1.0
 */

public class JdbcStatementMapper implements IStatementMapper {

    private DataHandleMapper baseMapper;
    private static final Integer PAGE_SIZE = 50;

    private static final Integer PAGE_NUM = 1;

    public JdbcStatementMapper(DataHandleMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public Map<String, Object> selectOne(String dataSourceId, String schema, String sql, Object params) {
        List<Map<String, Object>> maps = this.selectList(dataSourceId, schema, sql, params);
        return maps.size() > 0 ? maps.get(0) : new HashMap<>();
    }

    @Override
    public  List<Map<String, Object>> selectList(String dataSourceId, String schema, String sql, Object params) {
        // 列表默认查询15条
//        ApiPageInfo  page= this.selectPage(dataSourceId, schema, sql, params, PAGE_NUM, PAGE_SIZE);
//        return page.getList();
        return  null;
    }

    @Override
    public Object selectPage(String dataSourceId, String schema, String sql, Object params, int pageNum, int pageSize) {
        // 判断是否是预览运行SQL
//        String execType = null;
//        List<Map<String, Object>> list = new ArrayList<>();
//        try {
//            Map<String, Object> paramsMap = this.setParams(dataSourceId, schema, sql, params);
//            if (paramsMap.containsKey(DataSourceConstants.BASE_API_EXEC_TYPE)) {
//                execType = paramsMap.get(DataSourceConstants.BASE_API_EXEC_TYPE).toString();
//            }
//            Object pageSetup = paramsMap.get(DataSourceConstants.PAGE_SETUP);
//            int pageCount = pageSetup != null ? Integer.parseInt(pageSetup.toString()) : 0;
//            // 判断是否分页
//            if (0 != pageCount && !checkPage(sql)) {
//                PageHelper.startPage(pageNum, pageSize);
//            }
//            list = baseMapper.executeQuery(paramsMap);
//
//        }
//        catch (Exception e) {
//            Throwable cause = e.getCause();
//
//            if (execType == null) {
//                throw new CommonException(500, cause == null ? e.getMessage() : cause.getMessage());
//            } else {
//                Map<String, Object> errorMap = new HashMap<>();
//                errorMap.put("执行异常", "SQL执行失败：" + cause == null ? e.getMessage() : cause.getMessage());
//                list.add(errorMap);
//            }
//        } finally {
//            PageHelper.clearPage();
//            JdbcDataSourceRouter.remove();
//        }
//        return new ApiPageInfo<>(list, pageNum, pageSize);
          return null;
    }

    @Override
    public int insert(String dataSourceId, String schema, String sql, Object params) {
        Integer result;
        try {
            Map<String, Object> paramsMap = this.setParams(dataSourceId, schema, sql, params);
            result = baseMapper.executeInsert(paramsMap);
        } catch (Exception e) {
            throw new CommonException(ApiStatusEnum.API_SQL_ERROR.getCode(), ApiStatusEnum.API_SQL_ERROR.getMassage());
        } finally {
            // 移除线程
            JdbcDataSourceRouter.remove();
        }
        return result;
    }

    @Override
    public int delete(String dataSourceId, String schema, String sql, Object params) {
        try {
            Map<String, Object> paramsMap = this.setParams(dataSourceId, schema, sql, params);
            return baseMapper.executeDelete(paramsMap);
        } catch (Exception e) {
            throw new CommonException(ApiStatusEnum.API_SQL_ERROR.getCode(), ApiStatusEnum.API_SQL_ERROR.getMassage());
        } finally {
            // 移除线程
            JdbcDataSourceRouter.remove();
        }
    }

    @Override
    public int update(String dataSourceId, String schema, String sql, Object params) {
        try {
            Map<String, Object> paramsMap = this.setParams(dataSourceId, schema, sql, params);
            return baseMapper.executeUpdate(paramsMap);
        } catch (Exception e) {
            throw new CommonException(ApiStatusEnum.API_SQL_ERROR.getCode(), ApiStatusEnum.API_SQL_ERROR.getMassage());
        } finally {
            // 移除线程
            JdbcDataSourceRouter.remove();
        }
    }



    /**
     * 设置请求参数
     *
     * @param dataSourceId
     * @param schema
     * @param sql
     * @param params
     * @return
     */
    public Map<String, Object> setParams(String dataSourceId, String schema, String sql, Object params) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(DataSourceConstants.BASE_SQL, sql);
        if (params instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) params;
            paramsMap.putAll(map);
        }
        String dataSourceType = paramsMap.containsKey(DataSourceConstants.DATA_SOURCE_TYPE) ? paramsMap.get(DataSourceConstants.DATA_SOURCE_TYPE).toString() : "";
        // 设置线程数据源
        if (schema != null && !"".equals(schema)) {
            // dataSourceId:dataSourceType:schema
            dataSourceId = dataSourceId + ":"+ dataSourceType + ":" + schema;
        }
        JdbcDataSourceRouter.setDataSourceKey(dataSourceId);
        return paramsMap;
    }

    /**
     * 校验SQL是否包含分页
     * @param sql
     * @return
     */
    private boolean checkPage(String sql){
        // 匹配limit+ 数字的规则，mysql,tidb
        String mysql = "(?i)limit.*?\\d";
        // 匹配limit+ 数字的规则，postgres, sqlserver2012以上
        String postgres = "(?i)offset.*?\\d";
        // 匹配ROWNUM关键字分页，oracle
        String oracle ="(?i)ROWNUM.*?\\d";
        if (Pattern.compile(mysql).matcher(sql).find()){
            return true;
        }else if(Pattern.compile(postgres).matcher(sql).find()){
            return true;
        }else if(Pattern.compile(oracle).matcher(sql).find()){
            return true;
        }
        return false;
    }

}