package com.softwarelma.epe.p3.func;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeFuncFinalPrint extends EpeFuncAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecContent> listExecContent) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecContent", listExecContent);
        StringBuilder sb = new StringBuilder();

        for (EpeExecContent content : listExecContent) {
            sb.append(content.getContentInternal() == null ? null : content.getContentInternal().getStr());
        }

        String ret = sb.toString();

        if (execParams.isPrintToConsole()) {
            EpeAppLogger.logSystemOutPrintln(ret);
        }

        EpeExecResult execResult = new EpeExecResult(execParams.isPrintToConsole());
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(ret)));
        return execResult;
    }

}
