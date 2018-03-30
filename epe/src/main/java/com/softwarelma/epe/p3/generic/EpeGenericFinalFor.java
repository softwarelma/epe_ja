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
        String postMessage = "for, expected the list or list of list and optionally the index.";
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(listExecResult.size(), 1, 2, false, false, postMessage);

        if (isListStringAt(listExecResult, 0, postMessage)) {
            List<String> listStr = getListStringAt(listExecResult, 0, postMessage);

            if (listExecResult.size() == 1) {
                String str = listStr.remove(0);
                log(execParams, str);
                return createResult(str);
            }

            String ind = getStringAt(listExecResult, 1, postMessage);
            int i = EpeAppUtils.parseInt(ind);
            EpeAppUtils.checkRange(i, 0, listStr.size(), false, true, "index", postMessage);
            String str = listStr.get(i);
            log(execParams, str);
            return createResult(str);
        } else if (isListListStringAt(listExecResult, 0, postMessage)) {
            List<List<String>> listListStr = getListListStringAt(listExecResult, 0, postMessage);

            if (listExecResult.size() == 1) {
                List<String> listStr = listListStr.remove(0);
                log(execParams, listStr);
                return createResult(listStr);
            }

            String ind = getStringAt(listExecResult, 1, postMessage);
            int i = EpeAppUtils.parseInt(ind);
            EpeAppUtils.checkRange(i, 0, listListStr.size(), false, true, "index", postMessage);
            List<String> listStr = listListStr.get(i);
            log(execParams, listStr);
            return createResult(listStr);
        } else {
            throw new EpeAppException(postMessage);
        }
    }

}
