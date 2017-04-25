package com.softwarelma.epe.p3.echo;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeEchoFinalEcho extends EpeEchoAbstract {

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
                    str = retrieveEchoed(str, false);
                }

                sb.append(str);
            }
        }

        String ret = sb.toString();

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.log(ret);
        }

        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(ret)));
        return execResult;
    }

    public static String retrieveEchoed(String str, boolean reverse) throws EpeAppException {
        String sDoubleBackSlash = EpeAppUtils.getNotContainedString(str);

        if (reverse) {
            str = str.replace("\\", sDoubleBackSlash);
            str = str.replace("\"", "\\\"");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\t", "\\t");
            str = str.replace(sDoubleBackSlash, "\\\\");
            str = "\"" + str + "\"";
        } else {
            str = str.substring(1, str.length() - 1);
            str = str.replace("\\\\", sDoubleBackSlash);
            str = str.replace("\\\"", "\"");
            str = str.replace("\\n", "\n");
            str = str.replace("\\r", "\r");
            str = str.replace("\\t", "\t");
            str = str.replace(sDoubleBackSlash, "\\");
        }

        return str;
    }

}
