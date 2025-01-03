package com.lifetime.common.dataSource.driver.jdbc;

import com.lifetime.common.dataSource.config.JdbcDataSourceRouter;
import com.lifetime.common.dataSource.model.ColumnModel;
import com.lifetime.common.dataSource.model.SchemaModel;
import com.lifetime.common.dataSource.model.TableModel;
import com.lifetime.common.dataSource.spi.IMetaDataMapper;
import com.lifetime.common.exception.CommonException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/29-11:05
 * @description: com.lifetime.common.dataSource.driver.jdbc
 * @Version:1.0
 */

public class JdbcMetaDataMapper implements IMetaDataMapper {
    @Override
    public List<SchemaModel> getCatalogs(String dataSourceId) {
        List<SchemaModel> catalogs = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        DataSource dataSource = JdbcDataSourceRouter.getDataSource(dataSourceId);
        try {
            connection = dataSource.getConnection();
            resultSet = connection.getMetaData().getCatalogs();
            while (resultSet.next()) {
                String schemaName = resultSet.getString(1);
                if ("information_schema".equalsIgnoreCase(schemaName) || "performance_schema".equalsIgnoreCase(schemaName) || "pg_catalog".equals(schemaName)) {
                    continue;
                }
                SchemaModel schema = new SchemaModel();
                schema.setSchema(schemaName);
                schema.setCatalog(schemaName);
                schema.setDataSourceId(dataSourceId);
                catalogs.add(schema);
            }
        } catch (Exception e) {
            throw new IllegalStateException("query catalogs is error", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return catalogs;
    }

    @Override
    public List<SchemaModel> getSchemas(String dataSourceId, String catalog) {
        List<SchemaModel> schemas = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        DataSource dataSource = JdbcDataSourceRouter.getDataSource(dataSourceId);
        try {
            connection = dataSource.getConnection();
            resultSet = connection.getMetaData().getSchemas(catalog, null);
            while (resultSet.next()) {
                SchemaModel schema = new SchemaModel();
                String schemaName = resultSet.getString(1);
                schema.setSchema(schemaName);
                schema.setCatalog(catalog);
                if ("information_schema".equalsIgnoreCase(schemaName) || "performance_schema".equalsIgnoreCase(schemaName) || "pg_catalog".equals(schemaName)) {
                    continue;
                }
                schemas.add(schema);
            }
        } catch (Exception e) {
            throw new CommonException(500,"查询schema失败，请检查数据源是否正确");
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return schemas;
    }

    @Override
    public List<TableModel> getTables(String dataSourceId, String catalog, String schema) {
        List<TableModel> tables = new ArrayList<>();
        DataSource dataSource = JdbcDataSourceRouter.getDataSource(dataSourceId);
        String[] tableType = {"TABLE", "VIEW"};
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            // 获取表和视图
            resultSet = connection.getMetaData().getTables(catalog, schema, null, tableType);
            while (resultSet.next()) {
                TableModel table = new TableModel();
                table.setTableName(resultSet.getString("TABLE_NAME"));
                table.setRemarks(resultSet.getString("REMARKS"));
                table.setTableType(resultSet.getString("TABLE_TYPE"));
                table.setCatalog(resultSet.getString("TABLE_CAT"));
                table.setSchema(schema);
                tables.add(table);
            }
            // 获取存储过程
            try {
                ResultSet procedures = connection.getMetaData().getProcedures(catalog, schema, null);
                while (procedures.next()) {
                    TableModel table = new TableModel();
                    table.setTableName(procedures.getString("PROCEDURE_NAME"));
                    table.setRemarks(procedures.getString("REMARKS"));
                    table.setTableType("PROCEDURE");
                    table.setCatalog(procedures.getString("PROCEDURE_CAT"));
                    table.setSchema(schema);
                    tables.add(table);
                }
            }catch (Exception e1) {
            }
        } catch (Exception e) {
            throw new CommonException(500, "查询table失败，请检查数据源是否正确");
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return tables;
    }

    @Override
    public List<ColumnModel> getColumns(String dataSourceId, String catalog, String schema, String table) {
        List<ColumnModel> columns = new ArrayList<>();
        DataSource dataSource = JdbcDataSourceRouter.getDataSource(dataSourceId);
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            resultSet = connection.getMetaData().getColumns(catalog, schema, table, null);
            while (resultSet.next()) {
                ColumnModel column = new ColumnModel();
                column.setColumnName(resultSet.getString("COLUMN_NAME"));
                column.setRemarks(resultSet.getString("REMARKS"));
                column.setColumnType(resultSet.getString("TYPE_NAME"));
                column.setColumnSize(resultSet.getString("COLUMN_SIZE"));
                column.setColumnDefault(resultSet.getString("COLUMN_DEF"));
                column.setDecimalDigits(resultSet.getString("DECIMAL_DIGITS"));
                column.setCatalog(resultSet.getString("TABLE_CAT"));
                String columnType = column.getColumnType() == null ? "": column.getColumnType().toUpperCase();
                if (columnType.contains("DATE") || columnType.contains("TIME")) {
                    column.setTypeIcon("date");
                }else if(columnType.contains("INT") || columnType.contains("NUMBER")
                        || columnType.contains("FLOAT") || columnType.contains("DECIMAL")) {
                    column.setTypeIcon("int");
                }else{
                    column.setTypeIcon("str");
                }
                //column.setDatasourceId(dataSourceId);
                column.setSchema(schema);
                column.setTableName(table);
                columns.add(column);
            }
        } catch (Exception e) {
            throw new CommonException(500, "查询字段失败，请检查数据源是否正确");
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return columns;
    }
}
