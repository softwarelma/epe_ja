package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalImport extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "import, expected the var name to import.";
        String varName = this.getStringAt(listExecResult, 0, postMessage);
        EpeExecContent content = execParams.getGlobalParams().getMapVarNameExecContent().get(varName);
        EpeAppUtils.checkNull("content", content);
        execParams.getGlobalParams().getMapVarNameExecContent().remove(varName);
        this.log(execParams, "importing: " + varName + "=" + content.toString());
        return this.createResult(content);
    }

}
