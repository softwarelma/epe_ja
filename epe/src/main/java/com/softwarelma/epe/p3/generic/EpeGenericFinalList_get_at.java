package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecAbstract;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalList_get_at extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_get_at, expected the list (list of str or list of list), the index and optionally "
                + "the default element to return in case of no element at specified index.";
        // String index = this.getStringAt(listExecResult, 1, postMessage);
        // int ind = EpeAppUtils.parseInt(index);

        if (isStringAt(listExecResult, 0, postMessage)) {
            getListStringAt(listExecResult, 0, postMessage);// throwing
            return null;// unreachable
        }

        if (isListStringAt(listExecResult, 0, postMessage)) {
            String str = EpeGenericFinalList_get_str_at.doGetStrAt(execParams, listExecResult);
            return EpeExecAbstract.createResult(str);
        } else if (isListListStringAt(listExecResult, 0, postMessage)) {
            List<String> listStr = EpeGenericFinalList_get_list_at.doGetListAt(execParams, listExecResult);
            return EpeExecAbstract.createResult(listStr);
        } else {
            throw new EpeAppException(postMessage);
        }
    }

}
