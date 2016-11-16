package com.softwarelma.epe.p3.print;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_java_jar extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_java_jar, expected the java jar name, the physical RAM in MB and optionally the args.";
        String javaJarName = this.getStringAt(listExecResult, 0, postMessage);
        String physicalRamStr = this.getStringAt(listExecResult, 1, postMessage);
        long physicalRamInMB = Long.valueOf(physicalRamStr);
        String args = this.getStringAt(listExecResult, 2, postMessage, null);
        String str = retrieveJavaJarCommand(javaJarName, physicalRamInMB, args);
        this.log(execParams, str);
        return this.createResult(str);
    }

    /**
     * in MBytes
     */
    public static String retrieveJavaJarCommand(String javaJarName, long physicalRamInMB, String args) {
        long permSizeInMB = physicalRamInMB / 8;

        /*
         * una buona permsize migliora le performance quindi non la limitiamo a 512
         */
        // permSize = permSize < 256 ? 256 : permSize > 512 ? 512 : permSize;
        permSizeInMB = permSizeInMB < 256 && physicalRamInMB >= 768 ? 256 : permSizeInMB;

        args = EpeAppUtils.isEmpty(args) ? "" : " " + args;
        String command = "java -Xms" + physicalRamInMB + "M -Xmx" + physicalRamInMB + "M -Xss" + permSizeInMB
                + "M -XX:PermSize=" + permSizeInMB + "M -XX:MaxPermSize=" + permSizeInMB + "M -jar " + javaJarName
                + args;
        return command;
    }

}
