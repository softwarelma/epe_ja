package com.softwarelma.epe.p3.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDbFinalDb_datasource extends EpeDbAbstract {

    private static final Map<String, DataSource> mapUrlAndDataSource = new HashMap<>();

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "db_datasource, expected the URL, the username and the password.";
        String url = getStringAt(listExecResult, 0, postMessage);
        String username = getStringAt(listExecResult, 1, postMessage);
        String password = getStringAt(listExecResult, 2, postMessage);
        DataSource dataSource = retrieveOrCreateDataSource(url, username, password);
        List<String> listStr = new ArrayList<>();
        listStr.add("URL=" + url);
        listStr.add("username=" + username);
        log(execParams, listStr);
        return createResult(listStr, dataSource);
    }

    /**
     * Eg.
     * 
     * db_datasource("jdbc:oracle:thin:@//host:port/schema", "username", "password")
     * 
     * db_datasource("jdbc:mysql://host:port/schema", "username", "password")
     */
    public static DataSource retrieveOrCreateDataSource(String url, String username, String password)
            throws EpeAppException {
        EpeAppUtils.checkEmpty("url", url);
        EpeAppUtils.checkEmpty("username", username);
        EpeAppUtils.checkEmpty("password", password);
        DataSource dataSource = mapUrlAndDataSource.get(url);

        if (dataSource != null) {
            return dataSource;
        }

        try {
            if (url.startsWith("jdbc:oracle")) {
                OracleDataSource oracleDataSource;
                oracleDataSource = new OracleDataSource();
                oracleDataSource.setURL(url);// service_name syntax
                oracleDataSource.setUser(username);
                oracleDataSource.setPassword(password);
                dataSource = oracleDataSource;
            } else if (url.startsWith("jdbc:mysql")) {
                MysqlDataSource mysqlDataSource;
                mysqlDataSource = new MysqlDataSource();
                mysqlDataSource.setURL(url);// service_name syntax
                mysqlDataSource.setUser(username);
                mysqlDataSource.setPassword(password);
                dataSource = mysqlDataSource;
            } else {
                throw new EpeAppException("Invalid URL: " + url + ". Unknown DB vendor.");
            }
        } catch (EpeAppException e) {
            throw e;
        } catch (SQLException e) {
            throw new EpeAppException("Invalid URL or credentials. URL=" + url + " username=" + username, e);
        } catch (Exception e) {
            throw new EpeAppException("Invalid URL or credentials. URL=" + url + " username=" + username, e);
        }

        mapUrlAndDataSource.put(url, dataSource);
        return dataSource;
    }

}
