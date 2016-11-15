package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalExport extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "export, expected the var name to use to export and the content.";
        String varName = this.getStringAt(listExecResult, 0, postMessage);
        EpeExecContent content = this.getContentAt(listExecResult, 1, postMessage);
        execParams.getGlobalParams().getMapVarNameExecContent().put(varName, content);
        this.log(execParams, "exporting: " + varName + "=" + content.toString());
        return this.createEmptyResult();
    }

}
