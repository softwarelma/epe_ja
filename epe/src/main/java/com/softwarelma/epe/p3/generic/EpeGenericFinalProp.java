package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalProp extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        // String postMessage = "prop, expected...";
        listExecResult = this.toListStrIfPossible(listExecResult);
        setProp(listExecResult, true);
        return listExecResult.size() == 1 ? listExecResult.get(0) : this.createEmptyResult();
    }

    private List<EpeExecResult> toListStrIfPossible(List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        List<String> listStr = new ArrayList<>();

        for (EpeExecResult result : listExecResult) {
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();
            EpeAppUtils.checkNull("content", content);

            if (!content.isString()) {
                return listExecResult;
            }

            listStr.add(content.getStr());
        }

        EpeExecResult execResult = this.createResult(listStr);
        List<EpeExecResult> listExecResultRet = new ArrayList<>();
        listExecResultRet.add(execResult);
        return listExecResultRet;
    }

    public static void setProp(List<EpeExecResult> listExecResult, boolean prop) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);

        for (EpeExecResult result : listExecResult) {
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();
            EpeAppUtils.checkNull("content", content);
            content.setProp(prop);
        }
    }

}
