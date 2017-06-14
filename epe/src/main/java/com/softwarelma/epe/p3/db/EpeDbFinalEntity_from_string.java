package com.softwarelma.epe.p3.db;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDbFinalEntity_from_string extends EpeDbAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "entity_from_string, expected the URL, the username and the password.";
        String table = this.getStringAt(listExecResult, 0, postMessage);
        String entityStr = this.getStringAt(listExecResult, 1, postMessage);
        EpeDbEntity entity = newEntityFromString(table, entityStr);
        List<String> listStr = new ArrayList<>();
        //listStr.add("prop=" + value); //for future usage
        this.log(execParams, listStr);
        return this.createResult(listStr, entity);
    }

    public static EpeDbEntity newEntityFromString(String table, String entityStr)
            throws EpeAppException {
        EpeAppUtils.checkEmpty("table", table);
        EpeAppUtils.checkEmpty("entityStr", entityStr);
    }

    
}
