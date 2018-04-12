package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalSubstring extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "substring, expected text, begin index and optionally end index.";
        String str = getStringAt(listExecResult, 0, postMessage);
        String strBegin = getStringAt(listExecResult, 1, postMessage);
        String strEnd = getStringAt(listExecResult, 2, postMessage, null);
        int begin = EpeAppUtils.parseInt(strBegin);
        int end = strEnd == null ? -1 : EpeAppUtils.parseInt(strEnd);
        str = end == -1 ? str.substring(begin) : str.substring(begin, end);
        log(execParams, str);
        return createResult(str);
    }

}
