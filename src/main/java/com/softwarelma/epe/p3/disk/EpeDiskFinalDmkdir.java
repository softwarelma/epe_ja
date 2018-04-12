package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalDmkdir extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "dmkdir, expected 1-N dir names.";
        EpeAppUtils.checkRange(listExecResult.size(), 1, listExecResult.size(), false, false, "number-of-params",
                postMessage);

        for (int i = 0; i < listExecResult.size(); i++) {
            String dirName = getStringAt(listExecResult, i, postMessage);
            mkdir(dirName);
            log(execParams, (i + 1) + ". dmkdir \"" + dirName + "\"");
        }

        return createEmptyResult();
    }

    public static void mkdir(String dirName) throws EpeAppException {
        File dir = new File(dirName);

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
