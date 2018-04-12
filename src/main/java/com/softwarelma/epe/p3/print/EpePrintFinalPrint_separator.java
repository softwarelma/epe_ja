package com.softwarelma.epe.p3.print;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_separator extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_separator, expected a list with the param, external and internal separators "
                + "and the contents to print.";
        List<String> listStr = getListStringAt(listExecResult, 0, postMessage);
        String sepParam = listStr.size() > 0 ? listStr.get(0) : "";
        String sepExternal = listStr.size() > 1 ? listStr.get(1) : "";
        String sepInternal = listStr.size() > 2 ? listStr.get(2) : "";
        String str = retrievePrintableStrWithSeparators(sepParam, sepExternal, sepInternal, listExecResult, 1);
        log(execParams, str);
        return createResult(str);
    }

    public static String retrievePrintableStrWithSeparators(String sepParam, String sepExternal, String sepInternal,
            List<EpeExecResult> listExecResult, int startingIndex) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        StringBuilder sb = new StringBuilder();
        String sepParam2 = "";

        for (int i = startingIndex; i < listExecResult.size(); i++) {
            EpeExecResult result = listExecResult.get(i);
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();
            EpeAppUtils.checkNull("content", content);
            sb.append(sepParam2);
            sepParam2 = sepParam;
            sb.append(content.toString(sepExternal, sepInternal));
        }

        return sb.toString();
    }

}
