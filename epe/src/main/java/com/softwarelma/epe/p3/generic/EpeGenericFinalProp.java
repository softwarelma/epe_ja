package com.softwarelma.epe.p3.generic;

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
        setProp(listExecResult, true);
        return listExecResult.size() == 1 ? listExecResult.get(0) : this.createEmptyResult();
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
