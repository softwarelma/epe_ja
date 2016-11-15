package com.softwarelma.epe.p3.disk;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

/**
 * @author ellison
 *
 */
public final class EpeDiskFinalList_add extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_add, expected the list and the text to add.";
        List<String> listStr = this.getListStringAt(listExecResult, 0, postMessage);
        String text = this.getStringAt(listExecResult, 1, postMessage);
        listStr.add(text);
        this.log(execParams, listStr);
        return this.createResult(listStr);
    }

}
