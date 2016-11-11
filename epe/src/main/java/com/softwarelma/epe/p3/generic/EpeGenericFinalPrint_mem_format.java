package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalPrint_mem_format extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_mem_format, expected the mem size in Bytes.";
        String memBytesStr = this.getStringAt(listExecResult, 0, postMessage);
        long memBytes = Long.valueOf(memBytesStr);
        String str = formatMem(memBytes) + "";
        this.log(execParams, str);
        return this.createResult(str);
    }

    public static String formatMem(long memBytes) {
        return Math.round(((double) memBytes) / (1024 * 1024 * 1024)) + " GB\t\t" + (memBytes / (1024 * 1024))
                + " MB\t\t" + memBytes + " B";
    }

}
