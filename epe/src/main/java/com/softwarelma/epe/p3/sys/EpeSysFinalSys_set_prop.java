package com.softwarelma.epe.p3.sys;

import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeSysFinalSys_set_prop extends EpeSysAbstract {

    // non proxy syntax: localhost|127.0.0.1

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "sys_set_prop, expected 0 to N props strings like \"key=value\".";
        EpeAppUtils.checkNull("listExecResult", listExecResult);

        for (int i = 0; i < listExecResult.size(); i++) {
            String prop = getStringAt(listExecResult, i, postMessage);
            Map.Entry<String, String> keyValueVisualTrim = EpeAppUtils.retrieveKeyAndValueVisualTrim(prop);
            System.setProperty(keyValueVisualTrim.getKey(), keyValueVisualTrim.getValue());
        }

        return createEmptyResult();
    }

}
