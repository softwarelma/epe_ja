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
        String postMessage = "list_get_str_at, expected the list, the index and optionally the default str "
                + "to return in case of no element at specified index.";
        List<String> listStr = this.getListStringAt(listExecResult, 0, postMessage);
        String index = this.getStringAt(listExecResult, 1, postMessage);
        int ind = EpeAppUtils.parseInt(index);
        String str;

        if (listStr.size() <= ind) {// default needed
            if (listExecResult.size() > 2) {
                // retrieving the default str
                str = this.getStringAt(listExecResult, 2, postMessage);
            } else {
                throw new EpeAppException("Default needed. " + postMessage);
            }
        } else {
            EpeAppUtils.checkRange(ind, 0, listStr.size(), false, true, postMessage);
            str = listStr.get(ind);
        }

        this.log(execParams, str);
        return this.createResult(str);
    }

}
