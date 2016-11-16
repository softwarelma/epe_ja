package com.softwarelma.epe.p3.print;

import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.generic.EpeGenericFinalExec;

public final class EpePrintFinalPrint_mem_guessing extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_mem_guessing, expected the unit (B|MB), Btyes or MBytes, case insensitive.";
        String unit = this.getStringAt(listExecResult, 0, postMessage).toUpperCase();
        EpeAppUtils.checkContains(new String[] { "B", "MB" }, "unit", unit);
        boolean isBytes = unit.equals("B");
        long memBytesGuessing = retrieveMemBytesGuessing(execParams.getGlobalParams().isPrintToConsole(), isBytes);
        String str = memBytesGuessing + "";
        this.log(execParams, str);
        return this.createResult(str);
    }

    /**
     * if isBytes then in Bytes else in MBytes
     */
    public static long retrieveMemBytesGuessing(boolean doLog, boolean isBytes) throws EpeAppException {
        long physicalRamInBytes = EpePrintFinalPrint_mem_physical.retrievePhysicalRam();
        long physicalRamInMB = physicalRamInBytes / (1024 * 1024) / 2;
        String output;
        long secondsStart;
        long secondsEnd;

        do {
            String command = EpePrintFinalPrint_java_test.retrieveJavaTestCommand(physicalRamInMB);

            if (doLog) {
                EpeAppLogger.log("Tying command: " + command);
            }

            secondsStart = System.currentTimeMillis() / 1000;
            Map.Entry<Integer, String> exitAndOutput;
            exitAndOutput = EpeGenericFinalExec.execCommand(doLog, command);

            if (doLog) {
                EpeAppLogger.log("\tProcess exitValue: " + exitAndOutput.getKey());
            }

            output = exitAndOutput.getValue();
            secondsEnd = System.currentTimeMillis() / 1000;
            physicalRamInMB -= 100;
        } while (!output.toLowerCase().startsWith("java version ") && physicalRamInMB >= 500
                && secondsEnd - secondsStart < 60 // && output.length() < 2000
        );

        physicalRamInMB += 100;
        return isBytes ? physicalRamInMB * 1024 * 1024 : physicalRamInMB;
    }

}
