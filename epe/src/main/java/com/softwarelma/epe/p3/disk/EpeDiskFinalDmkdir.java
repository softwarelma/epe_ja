package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalDmkdir extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "dmkdir, expected the dir name.";
        String dirName = this.getStringAt(listExecResult, 0, postMessage);
        this.doMkdir(dirName);
        this.log(execParams, "dmkdir \"" + dirName + "\"");
        return this.createEmptyResult();
    }

    protected void doMkdir(String dirName) throws EpeAppException {
        File dir = new File(dirName);

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
