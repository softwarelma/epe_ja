package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalTo_unicode extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "to_unicode, expected the text.";
        String text = getStringAt(listExecResult, 0, postMessage);
        String str = retrieveToUnicode(text);
        log(execParams, str);
        return createResult(str);
    }

    public static String retrieveToUnicode(String text) {
        StringBuilder str = new StringBuilder();
        String s;
        int c;

        for (int i = 0; i < text.length(); i++) {
            c = text.charAt(i);
            s = Integer.toHexString(c);
            s = "\\u" + "0000".substring(s.length()) + s;
            str.append(s);
        }

        return str.toString();
    }

}
