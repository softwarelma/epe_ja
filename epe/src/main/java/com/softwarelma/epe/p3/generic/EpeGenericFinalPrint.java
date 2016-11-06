package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalPrint extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        StringBuilder sb = new StringBuilder();

        for (EpeExecResult result : listExecResult) {
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();
            sb.append(content.getContentInternal() == null ? null : content.getContentInternal().getStr());
        }

        String ret = sb.toString();

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.logSystemOutPrintln(ret);
        }

        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(ret)));
        return execResult;
    }

}
