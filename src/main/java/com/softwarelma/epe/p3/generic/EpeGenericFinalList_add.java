package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.disk.EpeDiskAbstract;

/**
 * @author ellison
 *
 */
public final class EpeGenericFinalList_add extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_add, expected the list and the text to add.";
        List<String> listStr = getListStringAt(listExecResult, 0, postMessage);
        String text = getStringAt(listExecResult, 1, postMessage);
        listStr.add(text);
        log(execParams, listStr);
        return createResult(listStr);
    }

}
