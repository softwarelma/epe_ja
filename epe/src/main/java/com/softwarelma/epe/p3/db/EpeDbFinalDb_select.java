package com.softwarelma.epe.p3.db;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
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

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDbFinalDb_select extends EpeDbAbstract {

    public static final String PROP_HEADER = "header";
    public static final String PROP_LIMIT = "limit";
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
        List<List<String>> listListStr = new ArrayList<>();

        for (int i = 1; i < listExecResult.size(); i++) {
            if (!this.isPropAt(listExecResult, i, postMessage)) {
                String select = this.getStringAt(listExecResult, i, postMessage);
                readQuery(dataSource, select, limitStr, avoidingClasses, listListStr, header);
            }
        }

        this.log(execParams, listListStr, null);
        return this.createResult(listListStr, null);
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

    
    
    *//read as obj 4 ewf
    
    public static void readQuery(DataSource dataSource, String select, String limitStr, String avoidingClasses,
            List<List<String>> listListStr, boolean header) throws EpeAppException {
        EpeAppUtils.checkNull("dataSource", dataSource);
        EpeAppUtils.checkNull("select", select);
        EpeAppUtils.checkNull("listListStr", listListStr);
        select = addLimits(dataSource, select, limitStr);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            readResult(resultSet, listListStr, header, avoidingClasses);
        } catch (Exception e) {
            throw new EpeAppException("retrieveResult with select: " + select, e);
        }
    }

    public static void readResult(ResultSet resultSet, List<List<String>> listListStr, boolean header,
            String avoidingClasses) throws EpeAppException {
        EpeAppUtils.checkNull("resultSet", resultSet);
        EpeAppUtils.checkNull("listListStr", listListStr);

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            if (header) {
                List<String> listEmtpy = new ArrayList<String>();
                List<String> colNames = new ArrayList<String>();

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    // listEmtpy.add("");
                    colNames.add(resultSetMetaData.getColumnName(i + 1));
                }

                listListStr.add(listEmtpy);
                listListStr.add(colNames);
            }

            while (resultSet.next()) {
                List<String> listStr = new ArrayList<>();

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    Object obj = resultSet.getObject(i + 1);
                    listStr.add(toString(resultSetMetaData.getColumnName(i + 1), obj, avoidingClasses));
                }

                listListStr.add(listStr);
            }
        } catch (Exception e) {
            throw new EpeAppException("Reading result set", e);
        }
    }

    public static String toString(String columnName, Object obj, String avoidingClasses) throws EpeAppException {
        EpeAppUtils.checkEmpty("columnName", columnName);
        avoidingClasses = avoidingClasses == null ? "" : "," + avoidingClasses.trim() + ",";

        if (obj == null) {
            return "";
        }

        Class<?> clazz = obj.getClass();
        String str;

        if (obj instanceof String) {
            str = obj.toString();
        } else if (obj instanceof BigDecimal) {
            str = ((BigDecimal) obj).toPlainString();
        } else if (obj instanceof Integer) {
            str = ((Integer) obj).toString();
        } else if (obj instanceof Timestamp) {
            Timestamp ts = (Timestamp) obj;
            str = new SimpleDateFormat("yyyyMMddHHmmss").format(ts);
            str = str.endsWith("000000") ? str.substring(0, 8) : str;
            System.out.println("timestamp=" + str);
        } else if (obj instanceof Clob) {
            if (avoidingClasses.contains("," + Clob.class.getName() + ",")) {
                return "[CLOB]";
            }

            try {
                Clob clob = (Clob) obj;
                str = clob.getSubString(1, (int) clob.length());
            } catch (SQLException e) {
                throw new EpeAppException("To string for column " + columnName, e);
            }
        } else {
            // str = obj.toString();
            String className = clazz.getName();
            throw new EpeAppException("Unknown class " + className + " for column " + columnName);
        }

        return str;
    }

}
