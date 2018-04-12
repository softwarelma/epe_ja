package com.softwarelma.epe.p3.print;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.generic.EpeGenericFinalList_funcs;

public final class EpePrintFinalPrint_funcs extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_funcs, expected optionally the package (db, disk, echo, generic, print, xml, etc.).";
        String pack = getStringAt(listExecResult, 0, postMessage, null);
        List<String> listFunc = pack == null ? EpeGenericFinalList_funcs.retriveListFuncAll()
                : EpeGenericFinalList_funcs.retriveListFunc(pack);
        StringBuilder sb = new StringBuilder();

        for (String func : listFunc) {
            sb.append(func);
            sb.append("\n");
        }

        String str = sb.toString();
        log(execParams, str);
        return createResult(str);
    }

}
