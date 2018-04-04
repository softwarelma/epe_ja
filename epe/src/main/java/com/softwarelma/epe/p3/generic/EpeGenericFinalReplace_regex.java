package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalReplace_regex extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "replace_regex, expected text, regex and replacement.";
        String text = getStringAt(listExecResult, 0, postMessage);
        String target = getStringAt(listExecResult, 1, postMessage);
        String replacement = getStringAt(listExecResult, 2, postMessage);
        String str = text.replaceAll(target, replacement);
        log(execParams, str);
        return createResult(str);
    }

}
