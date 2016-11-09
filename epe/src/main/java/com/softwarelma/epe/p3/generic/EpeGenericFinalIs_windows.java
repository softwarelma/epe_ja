package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalIs_windows extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        String str = isWindows() + "";
        this.log(execParams, str);
        return this.createResult(str);
    }

    public static boolean isWindows() {
        return (EpeGenericFinalOs_name.retrieveOsName().toLowerCase().indexOf("win") >= 0);
    }

}
