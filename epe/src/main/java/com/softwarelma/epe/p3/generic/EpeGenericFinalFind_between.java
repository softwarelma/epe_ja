package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalFind_between extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "find_between, expected text, prefix and suffix.";
        String text = this.getStringAt(listExecResult, 0, postMessage);
        String prefix = this.getStringAt(listExecResult, 1, postMessage);
        String suffix = this.getStringAt(listExecResult, 2, postMessage);
        String str = this.findBetween(text, prefix, suffix);
        this.log(execParams, str);
        return this.createResult(str);
    }

    private String findBetween(String text, String prefix, String suffix) {
        int indPrefix = text.indexOf(prefix);
        int indSuffix = text.indexOf(suffix);

        if (indPrefix != -1 && indSuffix != -1 && indPrefix <= indSuffix) {
            return text.substring(indPrefix + prefix.length(), indSuffix);
        }

        return null;
    }

}
