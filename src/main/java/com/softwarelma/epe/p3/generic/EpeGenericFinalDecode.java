package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalDecode extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "decode, expected text.";
        List<String> listStr = getAsListString(listExecResult, postMessage, false);
        String str = decode(listStr, postMessage);
        log(execParams, str);
        return createResult(str);
    }

    public static String decode(List<String> listStr, String postMessage) throws EpeAppException {
        EpeAppUtils.checkRange(listStr.size(), 3, listStr.size(), false, false, "listStr", postMessage);
        String text = listStr.get(0);
        boolean existsDefault = listStr.size() % 2 == 0;
        String defaultText = listStr.size() % 2 == 0 ? listStr.get(listStr.size() - 1) : null;
        String target, replacement;

        for (int i = 2; i < listStr.size(); i = i + 2) {
            target = listStr.get(i - 1);
            replacement = listStr.get(i);

            if (text == null && target == null || text != null && text.equals(target)) {
                return replacement;
            }
        }

        if (existsDefault) {
            return defaultText;
        }

        EpeAppUtils.checkNull("default", null);
        return null;
    }

}
