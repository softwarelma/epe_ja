package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalFrom_unicode extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "from_unicode, expected the text.";
        String text = this.getStringAt(listExecResult, 0, postMessage);
        String str = retrieveFromUnicode(text);
        this.log(execParams, str);
        return this.createResult(str);
    }

    public static String retrieveFromUnicode(String text) {
        text = text.replace("\\", "");
        String[] arr = text.split("u");
        String str = "";

        for (int i = 1; i < arr.length; i++) {
            int hexVal = Integer.parseInt(arr[i], 16);
            str += (char) hexVal;
        }

        return str;
    }

}
