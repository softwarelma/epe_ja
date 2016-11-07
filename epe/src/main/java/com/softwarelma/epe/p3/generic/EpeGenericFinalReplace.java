package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalReplace extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "replace, expected text, target and replacement.";

        if (this.isListStringAt(listExecResult, 0, postMessage)) {
            return this.doFuncListStringText(execParams, listExecResult, postMessage);
        } else if (this.isStringAt(listExecResult, 0, postMessage)) {
            return this.doFuncStringText(execParams, listExecResult, postMessage);
        }

        throw new EpeAppException("Text not valid. " + postMessage);
    }

    private EpeExecResult doFuncStringText(EpeExecParams execParams, List<EpeExecResult> listExecResult,
            String postMessage) throws EpeAppException {
        String text = this.getStringAt(listExecResult, 0, postMessage);
        String target = this.getStringAt(listExecResult, 1, postMessage);

        if (this.isStringAt(listExecResult, 2, postMessage)) {
            String replacement = this.getStringAt(listExecResult, 2, postMessage);
            String str = text.replace(target, replacement);
            this.log(execParams, str);
            return this.createResult(str);
        }

        List<String> listReplacement = this.getListStringAt(listExecResult, 2, postMessage);
        List<String> listRet = new ArrayList<>();

        for (String replacement : listReplacement) {
            String str = text.replace(target, replacement);
            this.log(execParams, str);
            listRet.add(str);
        }

        return this.createResult(listRet);
    }

    private EpeExecResult doFuncListStringText(EpeExecParams execParams, List<EpeExecResult> listExecResult,
            String postMessage) throws EpeAppException {
        List<String> listText = this.getListStringAt(listExecResult, 0, postMessage);
        List<String> listRet = new ArrayList<>();

        for (String text : listText) {
            String target = this.getStringAt(listExecResult, 1, postMessage);
            String replacement = this.getStringAt(listExecResult, 2, postMessage);
            String str = text.replace(target, replacement);
            this.log(execParams, str);
            listRet.add(str);
        }

        return this.createResult(listRet);
    }

}
