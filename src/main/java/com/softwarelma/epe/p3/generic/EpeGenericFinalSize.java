package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalSize extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "size, expected the str, list or list of list.";
        String str;

        if (isStringAt(listExecResult, 0, postMessage)) {
            str = getStringAt(listExecResult, 0, postMessage);
            str = str.length() + "";
        } else if (isListStringAt(listExecResult, 0, postMessage)) {
            List<String> listStr = getListStringAt(listExecResult, 0, postMessage);
            str = listStr.size() + "";
        } else if (isListListStringAt(listExecResult, 0, postMessage)) {
            List<List<String>> listListStr = getListListStringAt(listExecResult, 0, postMessage);
            str = listListStr.size() + "";
        } else {
            throw new EpeAppException("Invalid param at 0");
        }

        log(execParams, str);
        return createResult(str);
    }

}
