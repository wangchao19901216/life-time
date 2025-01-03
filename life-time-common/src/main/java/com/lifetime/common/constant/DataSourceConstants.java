package com.lifetime.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/28-22:18
 * @description: com.lifetime.common.constant
 * @Version:1.0
 */
public class DataSourceConstants {

    /**
     * 需要指定catalog的数据源
     */
    public static final List<String> CATALOG_DATA_SOURCE = Arrays.asList("sybase","mysql","mariadb","doris","starrocks","tidb","tdsql","clickhouse");

    /**
     * SQL脚本
     */
    public final static String BASE_SQL = "base_sql";

    /**
     * SQL执行方式(预览)
     */
    public static final String BASE_API_EXEC_TYPE = "base_api_exec_type:";


    /**
     * 分页设置
     */
    public final static String PAGE_SETUP = "pageSetup";

    /**
     * 页码
     */
    public final static String PAGE_NUM = "pageNum";

    /**
     * 每页大小
     */
    public final static String PAGE_SIZE = "pageSize";

    /**
     * 数据源类型
     */
    public final static String DATA_SOURCE_TYPE = "data_source_type";



}
