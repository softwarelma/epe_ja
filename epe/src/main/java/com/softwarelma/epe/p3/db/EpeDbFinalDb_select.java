package com.softwarelma.epe.p3.db;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.proxy.annotation.GetCreator;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDbFinalDb_select extends EpeDbAbstract {

    public static final String PROP_HEADER = "header";
    public static final String PROP_LIMIT = "limit";
    public static final String PROP_RESULT_AS_ENTITY = "result_as_entity";
    public static final int PROP_LIMIT_DEFAULT = 10;
    public static final String PROP_AVOIDING_CLASSES = "avoiding_classes";
    private static final Map<String, DataSource> mapUrlAndDataSource = new HashMap<>();

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "db_select, expected the data source and the select.";
        DataSource dataSource = this.getDataSourceAt(listExecResult, 0, postMessage);
        String limitStr = retrievePropValueOrNull("db_select", listExecResult, PROP_LIMIT);
        String avoidingClasses = retrievePropValueOrNull("db_select", listExecResult, PROP_AVOIDING_CLASSES);
        String headerStr = retrievePropValueOrDefault("db_select", listExecResult, PROP_HEADER, "false");
        EpeAppUtils.checkContains(new String[] { "true", "false" }, "header", headerStr);
        boolean header = headerStr.equals("true");
        String resultAsEntityStr = this.retrievePropValueOrDefault("db_select", listExecResult, PROP_RESULT_AS_ENTITY,
                "false");
        boolean resultAsEntity = EpeAppUtils.parseBoolean(resultAsEntityStr);

        if (resultAsEntity) {
            List<EpeDbEntity> listEntity = retrieveListEntity(listExecResult, postMessage, dataSource, limitStr,
                    avoidingClasses, header);
            // this.log(execParams, listEntity);
            List<String> listStr = new ArrayList<>();// for future usage
            return this.createResult(listStr, listEntity);
        } else {
            List<List<String>> listListStr = retrieveListListStr(listExecResult, postMessage, dataSource, limitStr,
                    avoidingClasses, header);
            this.log(execParams, listListStr, null);
            return this.createResult(listListStr, null);
        }
    }

    public List<List<String>> retrieveListListStr(List<EpeExecResult> listExecResult, String postMessage,
            DataSource dataSource, String limitStr, String avoidingClasses, boolean header) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkEmpty("postMessage", postMessage);
        List<List<String>> listListStr = new ArrayList<>();

        for (int i = 1; i < listExecResult.size(); i++) {
            if (!this.isPropAt(listExecResult, i, postMessage)) {
                String select = this.getStringAt(listExecResult, i, postMessage);
                readQueryAsString(dataSource, select, limitStr, avoidingClasses, listListStr, header);
            }
        }

        return listListStr;
    }

    public List<EpeDbEntity> retrieveListEntity(List<EpeExecResult> listExecResult, String postMessage,
            DataSource dataSource, String limitStr, String avoidingClasses, boolean header) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkEmpty("postMessage", postMessage);
        List<EpeDbEntity> listEntity = new ArrayList<>();

        for (int i = 1; i < listExecResult.size(); i++) {
            if (!this.isPropAt(listExecResult, i, postMessage)) {
                String select = this.getStringAt(listExecResult, i, postMessage);
                readQueryAsEntity(dataSource, select, limitStr, avoidingClasses, listEntity, header);
            }
        }

        return listEntity;
    }

    public static void readQueryAsString(DataSource dataSource, String select, String limitStr, String avoidingClasses,
            List<List<String>> listListStr, boolean header) throws EpeAppException {
        EpeAppUtils.checkNull("dataSource", dataSource);
        EpeAppUtils.checkNull("select", select);
        EpeAppUtils.checkNull("listListStr", listListStr);
        select = addLimits(dataSource, select, limitStr);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            readResultAsString(resultSet, listListStr, header, avoidingClasses);
        } catch (Exception e) {
            throw new EpeAppException("retrieveResult with select: " + select, e);
        }
    }

    public static void readQueryAsEntity(DataSource dataSource, String select, String limitStr, String avoidingClasses,
            List<EpeDbEntity> listEntity, boolean header) throws EpeAppException {
        EpeAppUtils.checkNull("dataSource", dataSource);
        EpeAppUtils.checkNull("select", select);
        EpeAppUtils.checkNull("listEntity", listEntity);
        select = addLimits(dataSource, select, limitStr);
        String table = retrieveTable(select);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            readResultAsEntity(table, resultSet, listEntity, avoidingClasses);
        } catch (Exception e) {
            throw new EpeAppException("retrieveResult with select: " + select, e);
        }
    }

    // TODO use params for queries

    public static String retrieveTable(String select) {
        // TODO
        return null;
    }

    public static String addLimits(DataSource dataSource, String select, String limitStr) throws EpeAppException {
        EpeAppUtils.checkNull("dataSource", dataSource);
        EpeAppUtils.checkNull("select", select);
        String dataSourceClassName = dataSource.getClass().getName();
        int limit;

        try {
            limit = Integer.parseInt(limitStr);
            limit = limit < 1 || limit > 99999 ? PROP_LIMIT_DEFAULT : limit;
        } catch (NumberFormatException e) {
            limit = PROP_LIMIT_DEFAULT;
        }

        if (dataSourceClassName.endsWith(".OracleDataSource")) {
            return "SELECT * FROM (" + select + ") WHERE ROWNUM <= " + limit;
        } else if (dataSourceClassName.endsWith(".MysqlDataSource")) {
            // TODO
            return select;
        } else {
            throw new EpeAppException("Unknown data source class name: " + dataSourceClassName);
        }
    }

    public static void readResultAsString(ResultSet resultSet, List<List<String>> listListStr, boolean header,
            String avoidingClasses) throws EpeAppException {
        EpeAppUtils.checkNull("resultSet", resultSet);
        EpeAppUtils.checkNull("listListStr", listListStr);

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            if (header) {
                // List<String> listEmtpy = new ArrayList<String>();
                List<String> colNames = new ArrayList<String>();

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    // listEmtpy.add("");
                    colNames.add(resultSetMetaData.getColumnName(i + 1));
                }

                // listListStr.add(listEmtpy);
                listListStr.add(colNames);
            }

            while (resultSet.next()) {
                List<String> listStr = new ArrayList<>();

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    Object obj = resultSet.getObject(i + 1);
                    listStr.add(EpeDbFinalEntity_to_string.toString(resultSetMetaData.getColumnName(i + 1), obj,
                            avoidingClasses));
                }

                listListStr.add(listStr);
            }
        } catch (Exception e) {
            throw new EpeAppException("Reading result set", e);
        }
    }

    public static void readResultAsEntity(String table, ResultSet resultSet, List<EpeDbEntity> listEntity,
            String avoidingClasses) throws EpeAppException {
        EpeAppUtils.checkNull("resultSet", resultSet);
        EpeAppUtils.checkNull("listEntity", listEntity);

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            EpeDbMetaDataEntity metaData = readResultMetaData(table, resultSetMetaData);
            Map<String, Object> mapAttAndValue;
            EpeDbEntity entity;

            while (resultSet.next()) {
                mapAttAndValue = new HashMap<>();

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    Object obj = resultSet.getObject(i + 1);
                    mapAttAndValue.put(resultSetMetaData.getColumnName(i + 1), obj);
                }

                entity = new EpeDbEntity(metaData, mapAttAndValue);
                listEntity.add(entity);
            }
        } catch (EpeAppException e) {
            throw e;
        } catch (Exception e) {
            throw new EpeAppException("Reading result set", e);
        }
    }

    public static EpeDbMetaDataEntity readResultMetaData(String table, ResultSetMetaData resultSetMetaData)
            throws EpeAppException {
        EpeAppUtils.checkEmpty("table", table);
        EpeAppUtils.checkNull("resultSetMetaData", resultSetMetaData);
        List<String> listAtrribute = new ArrayList<String>();
        Map<String, EpeDbMetaDataColumn> mapAttAndMetaAtt = new HashMap<>();
        EpeDbMetaDataColumn metaDataColumn;
        String atrribute;

        try {
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                atrribute = resultSetMetaData.getColumnName(i + 1);
                listAtrribute.add(atrribute);
                metaDataColumn = readResultMetaDataColumn(resultSetMetaData, i + 1);
                mapAttAndMetaAtt.put(atrribute, metaDataColumn);
            }
        } catch (EpeAppException e) {
            throw e;
        } catch (Exception e) {
            throw new EpeAppException("Reading result set meta data", e);
        }

        EpeDbMetaDataEntity metaData = new EpeDbMetaDataEntity(table, listAtrribute, mapAttAndMetaAtt);
        return metaData;
    }

    public static EpeDbMetaDataColumn readResultMetaDataColumn(ResultSetMetaData resultSetMetaData, int ind1Based)
            throws EpeAppException {
        EpeAppUtils.checkNull("resultSetMetaData", resultSetMetaData);

        try {
            String column = resultSetMetaData.getColumnName(ind1Based);
            String className;// TODO
            int precision = resultSetMetaData.getPrecision(ind1Based);
            int scale = resultSetMetaData.getScale(ind1Based);
            boolean nullable;// TODO
            Object defaultValue;// TODO

            EpeDbMetaDataColumn metaDataColumn = null;// TODO
            // new EpeDbMetaDataColumn(column, className, precision, scale, nullable,
            // defaultValue);
            return metaDataColumn;
        } catch (Exception e) {
            throw new EpeAppException("Reading result set column meta data", e);
        }
    }

    // //////////////////////////////////////////////

    // TODO test 4 tabs
    public static void getCols(Connection connection) throws EpeAppException {
        try {
            DatabaseMetaData dmd = connection.getMetaData();
            ResultSet rs = dmd.getTables(null, "SGE_MASTER", "C_DYQE%", null);
            ResultSetMetaData rsmd = rs.getMetaData();

            // for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            // System.out.print(rsmd.getColumnName(i) + "\t");
            // }

            while (rs.next()) {
                System.out.println();
                // for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                System.out.print(rs.getObject(3));
                // }
            }
            System.out.println();
        } catch (SQLException e) {
            throw new EpeAppException(e.getMessage(), e);
        }
    }

    // TODO test
    public String getColumnDefaultValue(Connection conn, String TableName, String ColumnName) throws Exception {
        String columnDefaultVal = "";
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getColumns(conn.getCatalog(), md.getUserName(), TableName, ColumnName);

        if (rs.next()) {
            columnDefaultVal = rs.getString("COLUMN_DEF");
        }

        System.out.println("Default Value of Column is " + columnDefaultVal);
        return columnDefaultVal;

    }

}
