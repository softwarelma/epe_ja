package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalEmpty extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "empty, expected str, list or list of list.";
        String str = isEmpty(listExecResult, postMessage) + "";
        log(execParams, str);
        return createResult(str);
    }

    public static boolean isEmpty(List<EpeExecResult> listExecResult, String postMessage) throws EpeAppException {
        boolean empty;

        if (isStringAt(listExecResult, 0, postMessage)) {
            String text = getStringAt(listExecResult, 0, postMessage);
            empty = text.isEmpty();
        } else if (isListStringAt(listExecResult, 0, postMessage)) {
            List<String> list = getListStringAt(listExecResult, 0, postMessage);
            empty = list.isEmpty();
        } else if (isListListStringAt(listExecResult, 0, postMessage)) {
            List<List<String>> listList = getListListStringAt(listExecResult, 0, postMessage);
            empty = listList.isEmpty();
        } else {
            throw new EpeAppException(postMessage);
        }

        return empty;
    }

}
