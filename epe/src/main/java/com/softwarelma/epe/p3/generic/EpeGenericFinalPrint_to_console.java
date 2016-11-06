package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalPrint_to_console extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkSize("listExecResult", listExecResult, 1);
        EpeExecResult result = listExecResult.get(0);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent execContent = result.getExecContent();
        EpeAppUtils.checkContains(new String[] { "true", "false" }, "true/false", execContent.getStr());

        execParams.getGlobalParams().setPrintToConsole(Boolean.valueOf(execContent.getStr()));

        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(execContent.getStr())));
        return execResult;
    }

}
