package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalImport_comment extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "import_comment, expected the comment key to import.";
        String key = getStringAt(listExecResult, 0, postMessage);
        String value = execParams.getGlobalParams().getMapComments().get(key);
        EpeAppUtils.checkNull("value", value);
        execParams.getGlobalParams().getMapComments().remove(key);
        log(execParams, "importing comment: " + key + "=" + value);
        return createResult(value);
    }

}
