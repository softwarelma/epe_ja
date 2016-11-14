package com.softwarelma.epe.p3.disk;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

/**
 * absolute names
 * 
 * @author ellison
 *
 */
public final class EpeDiskFinalClean_dir_name extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "clean_dir_name, expected dir name.";
        String dirName = this.getStringAt(listExecResult, 0, postMessage);
        dirName = EpeAppUtils.cleanDirName(dirName);
        this.log(execParams, dirName);
        return this.createResult(dirName);
    }

}
