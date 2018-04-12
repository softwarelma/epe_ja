package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalEquals extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "equals, expected string1 and string2, or list1 and list2 or listList1 and listList2.";

        if (isListListStringAt(listExecResult, 0, postMessage)) {
            return doFuncListList(execParams, listExecResult, postMessage);
        } else if (isListStringAt(listExecResult, 0, postMessage)) {
            return doFuncList(execParams, listExecResult, postMessage);
        } else if (isStringAt(listExecResult, 0, postMessage)) {
            return doFuncString(execParams, listExecResult, postMessage);
        }

        throw new EpeAppException("Parameters not valid. " + postMessage);
    }

    public EpeExecResult doFuncListList(EpeExecParams execParams, List<EpeExecResult> listExecResult,
            String postMessage) throws EpeAppException {
        List<List<String>> listList1 = getListListStringAt(listExecResult, 0, postMessage);
        List<List<String>> listList2 = getListListStringAt(listExecResult, 1, postMessage);
        String str = listList1.equals(listList2) + "";
        log(execParams, str);
        return createResult(str);
    }

    public EpeExecResult doFuncList(EpeExecParams execParams, List<EpeExecResult> listExecResult, String postMessage)
            throws EpeAppException {
        List<String> list1 = getListStringAt(listExecResult, 0, postMessage);
        List<String> list2 = getListStringAt(listExecResult, 1, postMessage);
        String str = list1.equals(list2) + "";
        log(execParams, str);
        return createResult(str);
    }

    public EpeExecResult doFuncString(EpeExecParams execParams, List<EpeExecResult> listExecResult, String postMessage)
            throws EpeAppException {
        String text1 = getStringAt(listExecResult, 0, postMessage);
        String text2 = getStringAt(listExecResult, 1, postMessage);
        String str = text1.equals(text2) + "";
        log(execParams, str);
        return createResult(str);
    }

}
