package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalReflow extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "reflow, expected the text (required).";
        String str = getStringAt(listExecResult, 0, postMessage);
        str = reflow(str);
        log(execParams, str);
        return createResult(str);
    }

    public static String reflow(String str) throws EpeAppException {
        EpeAppUtils.checkNull("str (to reflow)", str);
        str = str.replace("\r\n", "\n");
        String[] arrayLine = str.split("\n");
        String line0, line1;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arrayLine.length - 1; i++) {
            line0 = arrayLine[i];
            line1 = arrayLine[i + 1];
            sb.append(line0);

            if (!isEndOfPhrase(line0) && !line1.trim().isEmpty()) {
                if (!line0.endsWith(" "))
                    sb.append(" ");
            } else {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public static boolean isEndOfPhrase(String line) {
        line = line.trim();
        return line.isEmpty() || line.endsWith(".") || line.endsWith("?") || line.endsWith("!");
    }

}
