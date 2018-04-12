package com.softwarelma.epe.p3.print;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger.LEVEL;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_log_set extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_log_settings, expected no param or the nullable filename (currently "
                + EpeAppConstants.LOG_FILE_NAME + "), the nullable charset (currently "
                + EpeAppConstants.LOG_FILE_ENCODING + "), the nullable file-append flag (currently "
                + EpeAppConstants.LOG_FILE_APPEND + "), the console flag (currently " + EpeAppConstants.LOG_CONSOLE
                + ") and the level (currently " + EpeAppConstants.LOG_LEVEL + ").";

        EpeAppUtils.checkRange(listExecResult.size(), 5, 5, false, false, "number-of-arguments", postMessage);

        String fileName = getStringAt(listExecResult, 0, postMessage, null);
        String encoding = getStringAt(listExecResult, 1, postMessage, null);
        boolean append = "true".equals(getStringAt(listExecResult, 2, postMessage, null));
        boolean console = "true".equals(getStringAt(listExecResult, 3, postMessage, null));
        String levelStr = getStringAt(listExecResult, 4, postMessage, null);
        LEVEL level = levelStr == null || levelStr.isEmpty() ? null : LEVEL.valueOf(levelStr);

        List<String> listLogSettings = setLogSettings(fileName, encoding, append, console, level);
        log(execParams, listLogSettings);
        return createResult(listLogSettings);
    }

    public static List<String> setLogSettings(String fileName, String encoding, boolean append, boolean console,
            LEVEL level) throws EpeAppException {
        EpeAppConstants.LOG_FILE_NAME = fileName;
        EpeAppConstants.LOG_FILE_ENCODING = encoding;
        EpeAppConstants.LOG_FILE_APPEND = append;
        EpeAppConstants.LOG_CONSOLE = console;
        EpeAppConstants.LOG_LEVEL = level;
        return EpePrintFinalPrint_log_get.getLogSettings();
    }

}
