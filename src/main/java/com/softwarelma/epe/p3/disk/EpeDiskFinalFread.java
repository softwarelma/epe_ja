package com.softwarelma.epe.p3.disk;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFread extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "fread, expected the file name and optionally the encoding.";
        String filenameStr = getStringAt(listExecResult, 0, postMessage);
        String encodingStr = getStringAt(listExecResult, 1, postMessage, null);
        String str = fReadAsString(execParams.getGlobalParams().isPrintToConsole(), filenameStr, encodingStr);
        return createResult(str);
    }

    public static String fReadAsString(boolean doLog, String filenameStr, String encodingStr) throws EpeAppException {
        return EpeDiskFinalFread_encoding.fRead(doLog, filenameStr, encodingStr).getFileContent();
    }

}
