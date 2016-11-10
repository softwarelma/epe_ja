package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalEmpty extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "empty, expected str or list.";

        if (this.isStringAt(listExecResult, 0, postMessage)) {
            String text = this.getStringAt(listExecResult, 0, postMessage);
            String str = text.isEmpty() ? "true" : "false";
            this.log(execParams, str);
            return this.createResult(str);
        }

        List<String> list = this.getListStringAt(listExecResult, 0, postMessage);
        String str = list.isEmpty() ? "true" : "false";
        this.log(execParams, str);
        return this.createResult(str);
    }

}
