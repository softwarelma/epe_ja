package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalGoto extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "goto, expected label.";
        String label = this.getStringAt(listExecResult, 0, postMessage);
        int sentIndex = EpeAppUtils.parseInt(label);
        String str = sentIndex + "";

        if (listExecResult.size() > 1) {
            String condition = this.getStringAt(listExecResult, 1, postMessage);
            EpeAppUtils.checkContains(new String[] { "true", "false" }, "condition", condition);

            if (condition.equals("false")) {
                return this.createEmptyResult();
            }
        }

        execParams.getGlobalParams().setSentIndex(sentIndex);
        this.log(execParams, str);
        return this.createResult(str);
    }

}
