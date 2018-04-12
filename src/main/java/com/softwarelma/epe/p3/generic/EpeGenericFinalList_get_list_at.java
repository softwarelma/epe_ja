package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecAbstract;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalList_get_list_at extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        List<String> listStr = doGetListAt(execParams, listExecResult);
        log(execParams, listStr);
        return EpeExecAbstract.createResult(listStr);
    }

    public static List<String> doGetListAt(EpeExecParams execParams, List<EpeExecResult> listExecResult)
            throws EpeAppException {
        String postMessage = "list_get_list_at, expected the list of list, the index and optionally the default list "
                + "to return in case of no element at specified index.";
        List<List<String>> listListStr = EpeExecAbstract.getListListStringAt(listExecResult, 0, postMessage);
        String index = EpeExecAbstract.getStringAt(listExecResult, 1, postMessage);
        int ind = EpeAppUtils.parseInt(index);
        List<String> listStr;

        if (listListStr.size() <= ind) {// default needed
            if (listExecResult.size() > 2) {
                // retrieving the default listStr
                listStr = EpeExecAbstract.getListStringAt(listExecResult, 2, postMessage);
            } else {
                throw new EpeAppException("Default needed. " + postMessage);
            }
        } else {
            EpeAppUtils.checkRange(ind, 0, listListStr.size(), false, true, postMessage);
            listStr = listListStr.get(ind);
        }

        return listStr;
    }

}
