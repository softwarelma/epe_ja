package com.softwarelma.epe.p3.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDbFinalDb_select extends EpeDbAbstract {

    public static final String PROP_HEADER = "header";
    public static final String PROP_FOOTER = "footer";
    public static final String PROP_LIMIT = "limit";
    public static final String PROP_RESULT_AS_ENTITY = "result_as_entity";
    public static final int PROP_LIMIT_DEFAULT = 10;
    public static final String PROP_AVOIDING_CLASSES = "avoiding_classes";

    // comma-separated, eg "tables=t1" or "tables=t1,t2"
    public static final String PROP_TABLES = "tables";

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "db_select, expected the data source and the select.";
        DataSource dataSource = getDataSourceAt(listExecResult, 0, postMessage);
        String limitStr = retrievePropValueOrNull("db_select", listExecResult, PROP_LIMIT);
        String avoidingClasses = retrievePropValueOrNull("db_select", listExecResult, PROP_AVOIDING_CLASSES);

        String headerStr = retrievePropValueOrDefault("db_select", listExecResult, PROP_HEADER, "false");
        EpeAppUtils.checkContains(new String[] { "true", "false" }, "header", headerStr);
        boolean header = headerStr.equals("true");

        String footerStr = retrievePropValueOrDefault("db_select", listExecResult, PROP_FOOTER, "false");
        EpeAppUtils.checkContains(new String[] { "true", "false" }, "footer", footerStr);
        boolean footer = footerStr.equals("true");

        String resultAsEntityStr = retrievePropValueOrDefault("db_select", listExecResult, PROP_RESULT_AS_ENTITY,
                "false");
        boolean resultAsEntity = EpeAppUtils.parseBoolean(resultAsEntityStr);

        if (resultAsEntity) {
            List<EpeDbEntity> listEntity = new ArrayList<>();
            List<EpeDbMetaDataEntity> ListMetaData = retrieveListEntityFromExecResult(listExecResult, postMessage,
                    dataSource, limitStr, listEntity);
            // this.log(execParams, listEntity);
            List<String> listStr = new ArrayList<>();// for future usage
            return createResult(listStr, ListMetaData, listEntity);
        } else {
            List<List<String>> listListStr = retrieveListListStr(listExecResult, postMessage, dataSource, limitStr,
                    avoidingClasses, header, footer);
            log(execParams, listListStr, null);
            return createResult(listListStr, null);
        }
    }

    public List<List<String>> retrieveListListStr(List<EpeExecResult> listExecResult, String postMessage,
            DataSource dataSource, String limitStr, String avoidingClasses, boolean header, boolean footer)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkEmpty("postMessage", postMessage);
        List<List<String>> listListStr = new ArrayList<>();

        for (int i = 1; i < listExecResult.size(); i++) {
            if (!isPropAt(listExecResult, i, postMessage)) {
                String select = getStringAt(listExecResult, i, postMessage);
                readQueryAsString(dataSource, select, limitStr, avoidingClasses, listListStr, header, footer);
            }
        }

        return listListStr;
    }

    public List<EpeDbMetaDataEntity> retrieveListEntityFromExecResult(List<EpeExecResult> listExecResult,
            String postMessage, DataSource dataSource, String limitStr, List<EpeDbEntity> listEntity)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkEmpty("postMessage", postMessage);
        String[] arrayTable = this.retrieveTables(listExecResult);
        int index = 0;
        List<EpeDbMetaDataEntity> ListMetaData = new ArrayList<>();
        List<String> listSelect = new ArrayList<>();

        for (int i = 1; i < listExecResult.size(); i++) {// 0 for props
            if (!isPropAt(listExecResult, i, postMessage)) {// other props
                String select = getStringAt(listExecResult, i, postMessage);
                listSelect.add(select);
            }
        }

        retrieveListEntity(listSelect, arrayTable, postMessage, dataSource, limitStr, listEntity);
        return ListMetaData;
    }

    public List<EpeDbMetaDataEntity> retrieveListEntity(List<String> listSelect, String[] arrayTable,
            String postMessage, DataSource dataSource, String limitStr, List<EpeDbEntity> listEntity)
            throws EpeAppException {
        EpeAppUtils.checkNull("listSelect", listSelect);
        EpeAppUtils.checkEmpty("postMessage", postMessage);
        List<EpeDbMetaDataEntity> ListMetaData = new ArrayList<>();

        for (int i = 0; i < listSelect.size(); i++) {
            String select = listSelect.get(i);
            EpeAppUtils.checkRange(i, 0, arrayTable.length, false, true);
            String table = arrayTable[i];
            ListMetaData.add(readQueryAsEntity(dataSource, select, table, limitStr, listEntity));
        }

        return ListMetaData;
    }

    public String[] retrieveTables(List<EpeExecResult> listExecResult) throws EpeAppException {
        String tables = retrievePropValueOrNull("db_select", listExecResult, PROP_TABLES);
        EpeAppUtils.checkEmpty("tables", tables);
        String[] arrayTable = tables.split("\\,");
        return arrayTable;
    }

    public static void readQueryAsString(DataSource dataSource, String select, String limitStr, String avoidingClasses,
            List<List<String>> listListStr, boolean header, boolean footer) throws EpeAppException {
        EpeAppUtils.checkNull("dataSource", dataSource);
        EpeAppUtils.checkNull("select", select);
        EpeAppUtils.checkNull("listListStr", listListStr);
        select = addLimits(dataSource, select, limitStr);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            readResultAsString(resultSet, listListStr, header, footer, avoidingClasses);
        } catch (Exception e) {
            throw new EpeAppException("retrieveResult with select: " + select, e);
        }
    }

    public static EpeDbMetaDataEntity readQueryAsEntity(DataSource dataSource, String select, String table,
            String limitStr, List<EpeDbEntity> listEntity) throws EpeAppException {
        EpeAppUtils.checkNull("dataSource", dataSource);
        EpeAppUtils.checkNull("select", select);
        EpeAppUtils.checkNull("listEntity", listEntity);
        select = addLimits(dataSource, select, limitStr);
        EpeDbMetaDataEntity metaData;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            metaData = readResultAsEntity(connection, table, resultSet, listEntity);
        } catch (Exception e) {
            throw new EpeAppException("retrieveResult with select: " + select, e);
        }

        return metaData;
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
            return select + " limit 0, " + limit;
        } else {
            throw new EpeAppException("Unknown data source class name: " + dataSourceClassName);
        }
    }

    public static void readResultAsString(ResultSet resultSet, List<List<String>> listListStr, boolean header,
            boolean footer, String avoidingClasses) throws EpeAppException {
        EpeAppUtils.checkNull("resultSet", resultSet);
        EpeAppUtils.checkNull("listListStr", listListStr);

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            if (header) {
                // List<String> listEmtpy = new ArrayList<String>();
                List<String> colNames = new ArrayList<String>();

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    // listEmtpy.add("");
                    colNames.add(resultSetMetaData.getColumnLabel(i + 1));
                }

                // listListStr.add(listEmtpy);
                listListStr.add(colNames);
            }

            while (resultSet.next()) {
                List<String> listStr = new ArrayList<>(resultSetMetaData.getColumnCount());

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    Object obj = resultSet.getObject(i + 1);
                    listStr.add(EpeDbFinalEntity_to_string.toString(resultSetMetaData.getColumnLabel(i + 1), obj,
                            avoidingClasses));
                }

                listListStr.add(listStr);
            }

            if (footer) {
                listListStr.add(new ArrayList<String>());
            }
        } catch (Exception e) {
            throw new EpeAppException("Reading result set", e);
        }
    }

    public static EpeDbMetaDataEntity readResultAsEntity(Connection connection, String table, ResultSet resultSet,
            List<EpeDbEntity> listEntity) throws EpeAppException {
        EpeAppUtils.checkNull("resultSet", resultSet);
        EpeAppUtils.checkNull("listEntity", listEntity);
        EpeDbMetaDataEntity metaData;

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            metaData = readResultMetaData(connection, table, resultSetMetaData);
            String attribute;
            Map<String, Object> mapAttAndValue;
            EpeDbEntity entity;

            while (resultSet.next()) {
                mapAttAndValue = new HashMap<>();

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    Object obj = resultSet.getObject(i + 1);
                    attribute = resultSetMetaData.getColumnLabel(i + 1);
                    mapAttAndValue.put(attribute, obj);
                    metaData.setColumnClassNameIfNotNull(attribute, obj);
                }

                entity = new EpeDbEntity(metaData, mapAttAndValue);
                listEntity.add(entity);
            }
        } catch (EpeAppException e) {
            throw e;
        } catch (Exception e) {
            throw new EpeAppException("Reading result set", e);
        }

        return metaData;
    }

    public static EpeDbMetaDataEntity readResultMetaData(Connection connection, String table,
            ResultSetMetaData resultSetMetaData) throws EpeAppException {
        EpeAppUtils.checkEmpty("table", table);
        EpeAppUtils.checkNull("resultSetMetaData", resultSetMetaData);
        List<String> listAtrribute = new ArrayList<String>();
        Map<String, EpeDbMetaDataColumn> mapAttAndMetaAtt = new HashMap<>();
        EpeDbMetaDataColumn metaDataColumn;
        String atrribute;

        try {
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                atrribute = resultSetMetaData.getColumnLabel(i + 1);
                listAtrribute.add(atrribute);
                metaDataColumn = readResultMetaDataColumn(connection, table, resultSetMetaData, i + 1);
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

    public static EpeDbMetaDataColumn readResultMetaDataColumn(Connection connection, String table,
            ResultSetMetaData resultSetMetaData, int ind1Based) throws EpeAppException {
        EpeAppUtils.checkNull("resultSetMetaData", resultSetMetaData);

        try {
            String column = resultSetMetaData.getColumnLabel(ind1Based);
            String className = null;// when retrieving the objects
            int precision = resultSetMetaData.getPrecision(ind1Based);
            int scale = resultSetMetaData.getScale(ind1Based);
            boolean nullable = resultSetMetaData.isNullable(ind1Based) == ResultSetMetaData.columnNullable;
            String[] columnDefaultAndComment = retrieveColumnDefaultAndComment(connection, table, column);
            String defaultValue = columnDefaultAndComment == null ? null : columnDefaultAndComment[0];
            String comment = columnDefaultAndComment == null ? null : columnDefaultAndComment[1];

            EpeDbMetaDataColumn metaDataColumn = new EpeDbMetaDataColumn(column, className, precision, scale, nullable,
                    defaultValue, comment);
            return metaDataColumn;
        } catch (Exception e) {
            throw new EpeAppException("Reading result set column meta data", e);
        }
    }

    public static String[] retrieveColumnDefaultAndComment(Connection connection, String table, String column)
            throws EpeAppException {
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getColumns(connection.getCatalog(), md.getUserName(), table.toLowerCase(),
                    column.toLowerCase());
            boolean printEnable = false;

            if (rs.next()) {
                if (printEnable) {
                    printResultSetRecord(table, column, rs);
                }

                return new String[] { rs.getString("COLUMN_DEF"), rs.getString("REMARKS") };
            }

            rs = md.getColumns(connection.getCatalog(), md.getUserName(), table.toLowerCase(), column.toUpperCase());
            if (rs.next()) {
                if (printEnable) {
                    printResultSetRecord(table, column, rs);
                }

                return new String[] { rs.getString("COLUMN_DEF"), rs.getString("REMARKS") };
            }

            rs = md.getColumns(connection.getCatalog(), md.getUserName(), table.toUpperCase(), column.toLowerCase());
            if (rs.next()) {
                if (printEnable) {
                    printResultSetRecord(table, column, rs);
                }

                return new String[] { rs.getString("COLUMN_DEF"), rs.getString("REMARKS") };
            }

            rs = md.getColumns(connection.getCatalog(), md.getUserName(), table.toUpperCase(), column.toUpperCase());
            if (rs.next()) {
                if (printEnable) {
                    printResultSetRecord(table, column, rs);
                }

                return new String[] { rs.getString("COLUMN_DEF"), rs.getString("REMARKS") };
            }

            return null;
        } catch (SQLException e) {
            throw new EpeAppException("When retrieving metadata for " + table + "." + column, e);
        }
    }

    private static void printResultSetRecord(String table, String column, ResultSet rs) throws EpeAppException {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            EpeAppLogger.log("col " + column);

            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                EpeAppLogger.log("    " + rsmd.getColumnLabel(i + 1) + " = " + rs.getString(i + 1));
            }
        } catch (SQLException e) {
            throw new EpeAppException("When printing metadata for " + table + "." + column, e);
        }
    }

    public static Map<String, List<String>> retrieveMapTableAndListColumn(Connection connection, String table)
            throws EpeAppException {
        try {
            table = table == null ? "%" : table;
            DatabaseMetaData md = connection.getMetaData();
            List<String> listTable = new ArrayList<>();
            ResultSet rs = md.getTables(null, md.getUserName(), table.toUpperCase(), null);
            if (rs.next())
                rs.beforeFirst();
            else
                rs = md.getTables(null, md.getUserName(), table.toLowerCase(), null);
            while (rs.next())
                listTable.add(rs.getString(3));
            Map<String, List<String>> mapTableAndListColumn = new HashMap<>();

            for (String tableI : listTable) {
                List<String> listColumn = new ArrayList<>();
                mapTableAndListColumn.put(tableI.toUpperCase(), listColumn);
                rs = md.getColumns(null, md.getUserName(), tableI, null);
                while (rs.next())
                    listColumn.add(rs.getString("COLUMN_NAME").toUpperCase());
            }

            return mapTableAndListColumn;
        } catch (SQLException e) {
            throw new EpeAppException(e.getMessage(), e);
        }
    }

}
