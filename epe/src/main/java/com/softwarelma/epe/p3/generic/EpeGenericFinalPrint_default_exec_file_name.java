package com.softwarelma.epe.p3.generic;

import java.io.File;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalPrint_default_exec_file_name extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String str = retrieveDefaultExecFilename();
        this.log(execParams, str);
        return this.createResult(str);
    }

    public static String retrieveDefaultExecFilename() throws EpeAppException {
        String execFilename = null;

        if (EpeGenericFinalIs_windows.isWindows()) {
            execFilename = EpeAppConstants.EXEC_FILE_WIN;
        } else if (EpeGenericFinalIs_unix.isUnix()) {
            execFilename = EpeAppConstants.EXEC_FILE_LIN;
        } else if (EpeGenericFinalIs_mac.isMac()) {
            execFilename = EpeAppConstants.EXEC_FILE_LIN;
        } else if (EpeGenericFinalIs_solaris.isSolaris()) {
            execFilename = EpeAppConstants.EXEC_FILE_LIN;
        }

        EpeAppUtils.checkNull("execFilename (OS: " + EpeGenericFinalPrint_os_name.retrieveOsName() + ")", execFilename);
        File tmp = new File("tmp");

        if (!tmp.exists()) {
            tmp.mkdir();
        }

        execFilename = "tmp/" + execFilename;
        return execFilename;
    }

}
