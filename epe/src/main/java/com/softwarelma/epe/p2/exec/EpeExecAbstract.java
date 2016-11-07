package com.softwarelma.epe.p2.exec;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public abstract class EpeExecAbstract implements EpeExecInterface {

    protected void log(EpeExecParams execParams, String str) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.logSystemOutPrintln(str);
        }
    }

    protected EpeExecResult createEmptyResult() {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(null));
        return execResult;
    }

    protected EpeExecResult createResult(String str) {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(str)));
        return execResult;
    }

    protected String getStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        String str = content.getStr();
        EpeAppUtils.checkNull("str", str);

        return str;
    }

}
