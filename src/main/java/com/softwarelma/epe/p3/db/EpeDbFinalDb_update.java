package com.softwarelma.epe.p3.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDbFinalDb_update extends EpeDbAbstract {

    private static final Map<String, DataSource> mapUrlAndDataSource = new HashMap<>();

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "db_update, expected the data source and the select.";
        DataSource dataSource = getDataSourceAt(listExecResult, 0, postMessage);
        String update = getStringAt(listExecResult, 1, postMessage);
        String modifiedRows = executeUpdate(dataSource, update) + "";
        log(execParams, modifiedRows);
        return createResult(modifiedRows);
    }

    /**
     * @return the number of modified rows
     */
    public static int executeUpdate(DataSource dataSource, String update) throws EpeAppException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            int modifiedRows = preparedStatement.executeUpdate();
            // connection.commit();
            return modifiedRows;
        } catch (Exception e) {
            throw new EpeAppException("Invalid execute update: " + update, e);
        }
    }

}
