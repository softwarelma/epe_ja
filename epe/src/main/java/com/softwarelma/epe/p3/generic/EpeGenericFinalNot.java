package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalNot extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "replace_regex, expected text, regex and replacement.";
        String text = getStringAt(listExecResult, 0, postMessage);
        EpeAppUtils.checkContains(new String[] { "true", "false" }, "boolean", text);
        String str = text.equals("true") ? "false" : "true";
        log(execParams, str);
        return createResult(str);
    }

}
