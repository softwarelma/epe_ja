package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalSystem_prop extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "system_prop, expected 0 to N props strings like \"key=value\".";
        checkProp(listExecResult, postMessage);

        for (int i = 0; i < listExecResult.size(); i++) {
            String prop = this.getStringAt(listExecResult, i, postMessage);
            int ind = prop.indexOf("=");
            String propKey = prop.substring(0, ind);
            String propVal = prop.substring(ind + 1);
            System.setProperty(propKey, propVal);
        }

        return this.createEmptyResult();
    }

    public void checkProp(List<EpeExecResult> listExecResult, String postMessage) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);

        for (int i = 0; i < listExecResult.size(); i++) {
            String prop = this.getStringAt(listExecResult, i, postMessage);

            if (!prop.contains("=")) {
                throw new EpeAppException(postMessage + " Found: \"" + prop + "\".");
            }
        }
    }

}
