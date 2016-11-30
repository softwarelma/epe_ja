package com.softwarelma.epe.p3.db;

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

public final class EpeDbFinalDb_datasource extends EpeDbAbstract {

    private static final Map<String, DataSource> mapUrlAndDataSource = new HashMap<>();

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "db_datasource, expected the URL, the username and the password.";
        String url = this.getStringAt(listExecResult, 0, postMessage);
        String username = this.getStringAt(listExecResult, 1, postMessage);
        String password = this.getStringAt(listExecResult, 2, postMessage);
        DataSource dataSource = retrieveOrCreateDataSource(url, username, password);
        List<String> listStr = new ArrayList<>();
        listStr.add("URL=" + url);
        listStr.add("username=" + username);
        this.log(execParams, listStr);
        return this.createResult(listStr, dataSource);
    }

    public static DataSource retrieveOrCreateDataSource(String url, String username, String password)
            throws EpeAppException {
        EpeAppUtils.checkEmpty("url", url);
        EpeAppUtils.checkEmpty("username", username);
        EpeAppUtils.checkEmpty("password", password);
        DataSource dataSource = mapUrlAndDataSource.get(url);

        if (dataSource != null) {
            return dataSource;
        }

        if (url.startsWith("jdbc:oracle")) {
            try {
                OracleDataSource oracleDataSource;
                oracleDataSource = new OracleDataSource();

                // sintassi service_name
                oracleDataSource.setURL(url);

                oracleDataSource.setUser(username);
                oracleDataSource.setPassword(password);
                dataSource = oracleDataSource;
            } catch (SQLException e) {
                throw new EpeAppException("Invalid URL or credentials. URL=" + url + " username=" + username, e);
            }
        } else {
            throw new EpeAppException("Invalid URL: " + url + ". Unknown DB vendor.");
        }

        mapUrlAndDataSource.put(url, dataSource);
        return dataSource;
    }

}
