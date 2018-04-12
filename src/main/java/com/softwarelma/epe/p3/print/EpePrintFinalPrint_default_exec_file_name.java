package com.softwarelma.epe.p3.print;

import java.io.File;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.generic.EpeGenericFinalIs_mac;
import com.softwarelma.epe.p3.generic.EpeGenericFinalIs_solaris;
import com.softwarelma.epe.p3.generic.EpeGenericFinalIs_unix;
import com.softwarelma.epe.p3.generic.EpeGenericFinalIs_windows;

public final class EpePrintFinalPrint_default_exec_file_name extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String str = retrieveDefaultExecFilename();
        log(execParams, str);
        return createResult(str);
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

        EpeAppUtils.checkNull("execFilename (OS: " + EpePrintFinalPrint_os_name.retrieveOsName() + ")", execFilename);
        File tmp = new File("tmp");

        if (!tmp.exists()) {
            tmp.mkdir();
        }

        execFilename = "tmp/" + execFilename;
        return execFilename;
    }

}
