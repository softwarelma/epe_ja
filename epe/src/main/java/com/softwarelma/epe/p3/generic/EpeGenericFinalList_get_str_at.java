package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalList_get_str_at extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_get_str_at, expected the list and the index.";
        List<String> listStr = this.getListStringAt(listExecResult, 0, postMessage);
        String index = this.getStringAt(listExecResult, 1, postMessage);
        int ind = EpeAppUtils.parseInt(index);
        String str = listStr.get(ind);
        this.log(execParams, str);
        return this.createResult(str);
    }

}
