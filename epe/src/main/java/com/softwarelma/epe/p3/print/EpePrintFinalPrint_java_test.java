package com.softwarelma.epe.p3.print;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_java_test extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_java_test, expected the physical ram.";
        String physicalRamStr = getStringAt(listExecResult, 0, postMessage);
        long physicalRam = Long.valueOf(physicalRamStr);
        String str = retrieveJavaTestCommand(physicalRam);
        log(execParams, str);
        return createResult(str);
    }

    /**
     * in MB
     */
    public static String retrieveJavaTestCommand(long physicalRamInMB) {
        long permSize = physicalRamInMB / 8;

        /*
         * trying to not limit the perm to 512 for performance reasons
         */
        // permSize = permSize < 256 ? 256 : permSize > 512 ? 512 : permSize;
        permSize = permSize < 256 && physicalRamInMB >= 768 ? 256 : permSize;

        String command = "java -Xms" + physicalRamInMB + "M -Xmx" + physicalRamInMB + "M -Xss" + permSize
                + "M -XX:PermSize=" + permSize + "M -XX:MaxPermSize=" + permSize + "M -version";
        return command;
    }

}
