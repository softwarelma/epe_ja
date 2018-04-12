package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.disk.EpeDiskAbstract;

/**
 * @author ellison
 *
 */
public final class EpeGenericFinalList_new extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        List<String> listStr = new ArrayList<>();
        log(execParams, listStr);
        return createResult(listStr);
    }

}
