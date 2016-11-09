package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalEcho_to_list extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "echo_to_list.";
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        List<String> listStr = new ArrayList<>();

        for (int i = 0; i < listExecResult.size(); i++) {
            String str = this.getStringAt(listExecResult, i, postMessage);
            listStr.add(str);
        }

        this.log(execParams, listStr);
        return this.createResult(listStr);
    }

}
