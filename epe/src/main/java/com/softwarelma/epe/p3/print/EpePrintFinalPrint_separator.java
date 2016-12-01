package com.softwarelma.epe.p3.print;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_separator extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        String sepInternal, String sepExternal;
        String str = retrievePrintableStrWithSeparators(listExecResult);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.logSystemOutPrintln(str);
        }

        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(str)));
        return execResult;
    }

    public static String retrievePrintableStrWithSeparators(String sepInternal, String sepExternal,List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        StringBuilder sb = new StringBuilder();

        for (EpeExecResult result : listExecResult) {
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();
            sb.append(content.getContentInternal() == null ? null : content.getContentInternal().toString());
        }

        return sb.toString();
    }

}
