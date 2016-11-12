package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalFor extends EpeGenericAbstract {

    // ind = "0";
    // label0 = label();
    // dir = for(list_dirs, ind);
    // ind = inc(ind);
    // smaller = smaller(ind, list_dirs);
    // echo("");
    // goto(label0, smaller);

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "for, expected list and optionally index.";
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(listExecResult.size(), 1, 2, false, false, postMessage);
        List<String> listStr = this.getListStringAt(listExecResult, 0, postMessage);

        if (listExecResult.size() == 1) {
            String str = listStr.remove(0);
            this.log(execParams, str);
            return this.createResult(str);
        }

        String ind = this.getStringAt(listExecResult, 1, postMessage);
        int i = EpeAppUtils.parseInt(ind);
        EpeAppUtils.checkRange(i, 0, listStr.size(), false, true, postMessage);
        String str = listStr.get(i);
        this.log(execParams, str);
        return this.createResult(str);
    }

}
