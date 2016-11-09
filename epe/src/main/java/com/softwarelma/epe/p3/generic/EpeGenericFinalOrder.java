package com.softwarelma.epe.p3.generic;

import java.util.Arrays;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalOrder extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "order, expected list.";
        List<String> list = this.getListStringAt(listExecResult, 0, postMessage);
        list = this.order(list);
        this.log(execParams, list);
        return this.createResult(list);
    }

    private List<String> order(List<String> list) {
        String[] array = list.toArray(new String[] {});
        Arrays.sort(array);
        return Arrays.asList(array);
    }

}
