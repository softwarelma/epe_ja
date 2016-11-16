package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.print.EpePrintFinalPrint_os_name;

public final class EpeGenericFinalIs_unix extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        String str = isUnix() + "";
        this.log(execParams, str);
        return this.createResult(str);
    }

    public static boolean isUnix() {
        String os = EpePrintFinalPrint_os_name.retrieveOsName().toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
    }

}
