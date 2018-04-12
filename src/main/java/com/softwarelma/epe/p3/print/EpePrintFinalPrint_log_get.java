package com.softwarelma.epe.p3.print;

import java.util.Arrays;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_log_get extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        List<String> listLogSettings = getLogSettings();
        log(execParams, listLogSettings);
        return createResult(listLogSettings);
    }

    public static List<String> getLogSettings() {
        List<String> listLogSettings = Arrays.asList(EpeAppConstants.LOG_FILE_NAME, EpeAppConstants.LOG_FILE_ENCODING,
                EpeAppConstants.LOG_FILE_APPEND + "", EpeAppConstants.LOG_CONSOLE + "",
                EpeAppConstants.LOG_LEVEL.toString());
        return listLogSettings;
    }

}
