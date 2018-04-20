package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p2.prog.EpeProgParser;

public final class EpeGenericFinalClean_comment extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "clean_comment, expected str or list optionally containing some line-or-block comments.";
        List<String> listStr = getAsListString(listExecResult, postMessage);
        listStr = cleanComment(listStr);

        if (listStr.size() == 1) {
            String str = listStr.get(0);
            log(execParams, str);
            return createResult(str);
        }

        log(execParams, listStr);
        return createResult(listStr);
    }

    public static List<String> cleanComment(List<String> listStr) throws EpeAppException {
        List<String> listStrRet = new ArrayList<>();
        for (String str : listStr)
            listStrRet.add(cleanComment(str));
        return listStrRet;
    }

    public static String cleanComment(String str) throws EpeAppException {
        EpeAppUtils.checkNull("str", str);
        boolean lfAdded = false;

        if (!str.endsWith("\n")) {
            lfAdded = true;
            str = str + "\n";
        }

        EpeProgParser progParser = EpeProgParser.getInstance();
        str = progParser.cleanComment(str);
        if (lfAdded && str.endsWith("\n"))
            str = str.substring(0, str.length() - 1);
        return str;
    }

}
