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

    private static final Map<String, DataSource> mapUrlAndDataSource = new HashMap<>();

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "db_select, expected the data source and the select.";
        DataSource dataSource = this.getDataSourceAt(listExecResult, 0, postMessage);
        String select = this.getStringAt(listExecResult, 1, postMessage);
        String headerStr = this.getStringAt(listExecResult, 2, postMessage, null);
        boolean header = isHeader(headerStr);
        List<List<String>> listListStr = new ArrayList<>();
        readQuery(dataSource, select, listListStr, header);
        this.log(execParams, listListStr, null);
        return this.createResult(listListStr, null);
    }

    public static boolean isHeader(String headerStr) throws EpeAppException {
        if (headerStr == null) {
            return false;
        }

        String prefix = "header=";

        if (!headerStr.startsWith(prefix)) {
            throw new EpeAppException("Invalid optional header param (" + headerStr
                    + "), it should be like header=true");
        }

        headerStr = headerStr.substring(prefix.length());
        EpeAppUtils.checkContains(new String[] { "true", "false" }, "header", headerStr);
        return headerStr.equals("true");
    }

    public static void readQuery(DataSource dataSource, String select, List<List<String>> listListStr, boolean header)
            throws EpeAppException {
        EpeAppUtils.checkNull("dataSource", dataSource);
        EpeAppUtils.checkNull("select", select);
        EpeAppUtils.checkNull("listListStr", listListStr);

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
        List<String> colNames = header ? new ArrayList<String>() : null;

        try {
            while (resultSet.next()) {
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                List<String> listStr = new ArrayList<>();

                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    if (header) {
                        colNames.add(resultSetMetaData.getColumnName(i + 1));
                    }

                    Object o = resultSet.getObject(i + 1);
                    listStr.add(o == null ? "" : o + "");// FIXME to str
                }

                if (header) {
                    listListStr.add(colNames);
                }

                header = false;
                listListStr.add(listStr);
            }
        } catch (Exception e) {
            throw new EpeAppException("retrieveResult", e);
        }
    }

}
