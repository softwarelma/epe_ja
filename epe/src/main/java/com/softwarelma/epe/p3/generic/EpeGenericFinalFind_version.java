package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalFind_version extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "find_version, expected text.";
        String text = getStringAt(listExecResult, 0, postMessage);
        String str = retrieveVersion(text);
        log(execParams, str);
        return createResult(str);
    }

    public static String retrieveVersion(String str) throws EpeAppException {
        String ret = "";
        char c;
        boolean versFound = false;
        String digits = "1234567890";
        char dot = '.';

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (digits.contains(c + "")) {
                if (ret.contains(".")) {
                    versFound = true;
                }

                ret += c;
            } else if (dot == c) {
                if (ret.endsWith(".")) {
                    ret = "";
                    versFound = false;
                } else {
                    ret = ret.isEmpty() ? ret : ret + c;
                }
            } else {
                if (versFound) {
                    EpeAppUtils.checkEmpty("cleaning " + str, ret);
                    return ret.endsWith(".") ? ret.substring(0, ret.length() - 1) : ret;
                }

                ret = "";
                versFound = false;
            }
        }

        ret = ret.isEmpty() || !versFound ? "0" : ret;
        return ret.endsWith(".") ? ret.substring(0, ret.length() - 1) : ret;
    }

}
