package com.softwarelma.epe.p3.disk;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

/**
 * @author ellison
 */
public final class EpeDiskFinalUser_home_dir extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String prop = System.getProperty("user.home");
        prop = EpeAppUtils.cleanDirName(prop);
        log(execParams, prop);
        return createResult(prop);
    }

}
