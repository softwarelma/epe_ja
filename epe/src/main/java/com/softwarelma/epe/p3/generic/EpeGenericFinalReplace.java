package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalReplace extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "replace, expected text, targe and replacement.";
        String text = this.getStringAt(listExecResult, 0, postMessage);
        String target = this.getStringAt(listExecResult, 1, postMessage);
        String replacement = this.getStringAt(listExecResult, 2, postMessage);
        String str = text.replace(target, replacement);
        this.log(execParams, str);
        return this.createResult(str);
    }

}
