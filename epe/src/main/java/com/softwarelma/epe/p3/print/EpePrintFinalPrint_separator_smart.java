package com.softwarelma.epe.p3.print;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_separator_smart extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_separator_smart, expected a list with the param, external and internal separators "
                + "and the contents to print.";
        String sepParam = "\n\n";
        String sepExternal = "\n";
        String str = retrievePrintableStrWithSeparatorsSmart(sepParam, sepExternal, listExecResult);
        this.log(execParams, str);
        return this.createResult(str);
    }

    public static String retrievePrintableStrWithSeparatorsSmart(String sepParam, String sepExternal,
            List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        StringBuilder sb = new StringBuilder();
        String sepParam2 = "";

        for (int i = 0; i < listExecResult.size(); i++) {
            EpeExecResult result = listExecResult.get(i);
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();
            EpeAppUtils.checkNull("content", content);
            sb.append(sepParam2);
            sepParam2 = sepParam;
            List<Integer> listWidth = retrieveWidths(content);
            sb.append(content.toString(sepExternal, listWidth));
        }

        return sb.toString();
    }

    private static List<Integer> retrieveWidths(EpeExecContent content) throws EpeAppException {
        List<Integer> listWidth = new ArrayList<>();

        if (content.getContentInternal() == null) {
            listWidth.add(4);
            return listWidth;
        }

        EpeExecContentInternal contentInternal = content.getContentInternal();

        if (contentInternal.isString()) {
            listWidth.add(contentInternal.getStr().length());
            return listWidth;
        } else if (contentInternal.isListString()) {
            for (String str : contentInternal.getListStr()) {
                listWidth.add((str + "").length());
            }

            return listWidth;
        } else if (contentInternal.isListListString()) {
            for (List<String> listStr : contentInternal.getListListStr()) {
                retrieveWidths(listStr, listWidth);
            }

            return listWidth;
        } else {
            throw new EpeAppException("Unknown internal content type");
        }
    }

    private static void retrieveWidths(List<String> listStr, List<Integer> listWidth) {
        if (listStr == null) {
            return;
        }

        int width;

        for (int i = 0; i < listStr.size(); i++) {
            String str = listStr.get(i);
            width = (str + "").length();

            if (listWidth.size() < i + 1) {
                listWidth.add(width);
            } else {
                if (width > listWidth.get(i)) {
                    listWidth.set(i, width);
                }
            }
        }
    }

}
