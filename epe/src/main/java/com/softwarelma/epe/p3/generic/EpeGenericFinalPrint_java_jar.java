package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalPrint_java_jar extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_java_jar, expected the java jar name, the physical ram and optionally the args.";
        String javaJarName = this.getStringAt(listExecResult, 0, postMessage);
        String physicalRamStr = this.getStringAt(listExecResult, 1, postMessage);
        long physicalRam = Long.valueOf(physicalRamStr);
        String args = this.getStringAt(listExecResult, 2, postMessage, null);
        String str = getJavaJarCommand(javaJarName, physicalRam, args);
        this.log(execParams, str);
        return this.createResult(str);
    }

    public static String getJavaJarCommand(String javaJarName, long physicalRam, String args) {
        long permSize = physicalRam / 8;

        /*
         * una buona permsize migliora le performance quindi non la limitiamo a 512
         */
        // permSize = permSize < 256 ? 256 : permSize > 512 ? 512 : permSize;
        permSize = permSize < 256 && physicalRam >= 768 ? 256 : permSize;

        args = EpeAppUtils.isEmpty(args) ? "" : " " + args;
        String command = "java -Xms" + physicalRam + "M -Xmx" + physicalRam + "M -Xss" + permSize + "M -XX:PermSize="
                + permSize + "M -XX:MaxPermSize=" + permSize + "M -jar " + javaJarName + args;
        return command;
    }

}
