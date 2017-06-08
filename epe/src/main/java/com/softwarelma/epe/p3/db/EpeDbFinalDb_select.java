package com.softwarelma.epe.p3.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
    private static final Map<String, DataSource> mapUrlAndDataSource = new HashMap<>();

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "db_select, expected the data source and the select.";
        DataSource dataSource = this.getDataSourceAt(listExecResult, 0, postMessage);
        String limitStr = retrievePropValueOrNull("db_select", listExecResult, PROP_LIMIT);
        String headerStr = retrievePropValueOrDefault("db_select", listExecResult, PROP_HEADER, "false");
        EpeAppUtils.checkContains(new String[] { "true", "false" }, "header", headerStr);
        boolean header = headerStr.equals("true");
        List<List<String>> listListStr = new ArrayList<>();

        for (int i = 1; i < listExecResult.size(); i++) {
            if (!this.isPropAt(listExecResult, i, postMessage)) {
                String select = this.getStringAt(listExecResult, i, postMessage);
                readQuery(dataSource, select, limitStr, listListStr, header);
            }
        }

        this.log(execParams, listListStr, null);
        return this.createResult(listListStr, null);
        // TODO limit to prop
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
        } else {
            throw new EpeAppException("Unknown data source class name: " + dataSourceClassName);
        }
    }

    public static void readQuery(DataSource dataSource, String select, String limitStr, List<List<String>> listListStr,
            boolean header) throws EpeAppException {
        EpeAppUtils.checkNull("dataSource", dataSource);
        EpeAppUtils.checkNull("select", select);
        EpeAppUtils.checkNull("listListStr", listListStr);
        select = addLimits(dataSource, select, limitStr);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            readResult(resultSet, listListStr, header);
        } catch (Exception e) {
            throw new EpeAppException("retrieveResult with select: " + select, e);
        }
    }

    public static void readResult(ResultSet resultSet, List<List<String>> listListStr, boolean header)
            throws EpeAppException {
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
                    Object o = resultSet.getObject(i + 1);
                    listStr.add(o == null ? "" : o + "");// TODO to str, see date, decimals, etc
                }

                listListStr.add(listStr);
            }
        } catch (Exception e) {
            throw new EpeAppException("Reading result set", e);
        }
    }

}
