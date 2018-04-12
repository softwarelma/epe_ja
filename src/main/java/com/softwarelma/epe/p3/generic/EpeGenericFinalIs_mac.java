package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.print.EpePrintFinalPrint_os_name;

public final class EpeGenericFinalIs_mac extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        String str = isMac() + "";
        log(execParams, str);
        return createResult(str);
    }

    public static boolean isMac() {
        return (EpePrintFinalPrint_os_name.retrieveOsName().toLowerCase().indexOf("mac") >= 0);
    }

}
