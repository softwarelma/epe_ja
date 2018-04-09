package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalContains extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "contains, expected the text, the target and optionally the case-sensitive bool (default true).";
        String text = getStringAt(listExecResult, 0, postMessage);
        String target = getStringAt(listExecResult, 1, postMessage);
        String caseSensitiveStr = getStringAt(listExecResult, 2, postMessage, "true");
        boolean caseSensitive = EpeAppUtils.parseBoolean(caseSensitiveStr);
        String str = contains(text, target, caseSensitive) + "";
        log(execParams, str);
        return createResult(str);
    }

    public static boolean contains(String text, String target, boolean caseSensitive) throws EpeAppException {
        EpeAppUtils.checkNull("text", text);
        EpeAppUtils.checkNull("target", target);
        return caseSensitive ? text.contains(target) : text.toLowerCase().contains(target.toLowerCase());
    }

}
