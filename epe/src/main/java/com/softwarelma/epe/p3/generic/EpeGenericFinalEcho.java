package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalEcho extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        StringBuilder sb = new StringBuilder();

        for (EpeExecResult result : listExecResult) {
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();

            if (content.getContentInternal() == null) {
                sb.append(null + "");
            } else {
                String str = content.getContentInternal().toString();

                if (str != null && str.startsWith("\"") && str.endsWith("\"") && str.length() > 1) {
                    str = getReplacedSting(str, false);
                }

                sb.append(str);
            }
        }

        String ret = sb.toString();

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.logSystemOutPrintln(ret);
        }

        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(ret)));
        return execResult;
    }

    public static String getReplacedSting(String s, boolean reverse) throws EpeAppException {
        String sDoubleBackSlash = EpeAppUtils.getNotContainedString(s);

        if (reverse) {
            s = s.replace("\\", sDoubleBackSlash);
            s = s.replace("\"", "\\\"");
            s = s.replace("\n", "\\n");
            s = s.replace("\r", "\\r");
            s = s.replace("\t", "\\t");
            s = s.replace(sDoubleBackSlash, "\\\\");
            s = "\"" + s + "\"";
        } else {
            s = s.substring(1, s.length() - 1);
            s = s.replace("\\\\", sDoubleBackSlash);
            s = s.replace("\\\"", "\"");
            s = s.replace("\\n", "\n");
            s = s.replace("\\r", "\r");
            s = s.replace("\\t", "\t");
            s = s.replace(sDoubleBackSlash, "\\");
        }

        return s;
    }

}
