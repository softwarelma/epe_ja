package com.softwarelma.epe.p3.func;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeFuncFinalPrint_to_console extends EpeFuncAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecContent> listExecContent) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecContent", listExecContent);
        EpeAppUtils.checkSize("listExecContent", listExecContent, 1);
        EpeExecContent execContent = listExecContent.get(0);
        EpeAppUtils.checkContains(new String[] { "true", "false" }, "true/false", execContent.getStr());

        EpeExecResult execResult = new EpeExecResult(Boolean.valueOf(execContent.getStr()));
        execResult.setExecContent(new EpeExecContent(null));
        return execResult;
    }

}
