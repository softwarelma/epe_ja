package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalBigger extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "bigger, expected an integer or list and another integer or list.";
        int i1 = this.getIntOrSizeAt(listExecResult, 0, postMessage);
        int i2 = this.getIntOrSizeAt(listExecResult, 1, postMessage);
        String str = (i1 > i2) + "";
        this.log(execParams, str);
        return this.createResult(str);
    }

}
