package com.softwarelma.epe.p3.print;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_timestamp extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_timestamp, expected optionally the format.";
        String format = getStringAt(listExecResult, 0, postMessage, EpeAppConstants.TIMESTAMP_DEFAULT_FORMAT);
        String str = EpeAppUtils.retrieveTimestamp(format);
        log(execParams, str);
        return createResult(str);
    }

}
