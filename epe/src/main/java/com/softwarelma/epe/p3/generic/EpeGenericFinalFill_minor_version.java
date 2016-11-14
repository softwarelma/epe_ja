package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalFill_minor_version extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "fill_minor_version, expected text.";
        String text = this.getStringAt(listExecResult, 0, postMessage);
        String excepMessage = this.getStringAt(listExecResult, 1, postMessage, null);
        String[] versionsRange = text.split(",");
        EpeAppUtils.checkRange(versionsRange.length, 2, 2, false, false, postMessage);
        List<String> listStr = retrieveListFullRange(versionsRange[0], versionsRange[1], excepMessage);
        this.log(execParams, listStr);
        return this.createResult(listStr);
    }

    private static List<String> retrieveListFullRange(String versionStart, String versionEnd, String excepMessage)
            throws EpeAppException {
        EpeAppUtils.checkEmpty("versionStart", versionStart);
        EpeAppUtils.checkEmpty("versionEnd", versionEnd);
        List<String> listFullRange = new ArrayList<>();
        Map.Entry<String, String> filePathAndLast1 = EpeAppUtils.retrievePathAndLast(versionStart, "\\.");
        Map.Entry<String, String> filePathAndLast2 = EpeAppUtils.retrievePathAndLast(versionEnd, "\\.");
        String message = excepMessage == null ? "The value " + filePathAndLast1.getKey() + " should be equal to "
                + filePathAndLast2.getKey() + "." : excepMessage;
        EpeAppUtils.checkEquals("path prefix-version start", "path prefix-version end", filePathAndLast1.getKey(),
                filePathAndLast2.getKey(), message);
        int minorStart = EpeAppUtils.parseInt(filePathAndLast1.getValue());
        int minorEnd = EpeAppUtils.parseInt(filePathAndLast2.getValue());
        EpeAppUtils.checkMinorOrEqual(minorStart, minorEnd);

        for (int i = minorStart; i < minorEnd + 1; i++) {
            listFullRange.add(filePathAndLast1.getKey() + i);
        }

        return listFullRange;
    }

}
