package com.softwarelma.epe.p3.generic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalExec_shell extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        // String postMessage = "exec_shell.";
        String str = "ok";
        execShell();
        log(execParams, str);
        return createResult(str);
    }

    public static boolean isAlive(Process p) {
        try {
            p.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

    public static void execShell() throws EpeAppException {
        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-i");
            builder.redirectErrorStream(true); // so we can ignore the error stream
            Process process = builder.start();
            InputStream out = process.getInputStream();
            OutputStream in = process.getOutputStream();
            String typed = "";
            int ind = 0;

            byte[] buffer = new byte[4000];
            while (isAlive(process)) {
                int no = out.available();
                if (no > 0) {
                    // buffer = new byte[4000];
                    int n = out.read(buffer, 0, Math.min(no, buffer.length));
                    String output = new String(buffer, 0, n);

                    if (output.contains("\n")) {// FIXME or endsWith?
                        typed = output.substring(0, output.indexOf("\n"));
                        if (ind > 0)
                            EpeAppLogger.log(typed, null, null, false, true);
                        else if (output.contains("no job control in this shell"))
                            ind = -1;
                        ind = ind < 1 ? ind + 1 : ind;
                        EpeAppLogger.log(output);
                        // System.out.println(output);
                    }
                }

                int ni = System.in.available();
                if (ni > 0) {
                    // buffer = new byte[4000];
                    int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
                    String input = new String(buffer, 0, n);
                    if (input.startsWith("exit"))
                        EpeAppLogger.log("exit", null, null, false, true);
                    in.write(buffer, 0, n);
                    in.flush();
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }

            String exit = process.exitValue() + "";
            // EpeAppLogger.log(typed, null, null, false, true);
            // System.out.println(exit);
            EpeAppLogger.log(exit);
        } catch (IOException e) {
            throw new EpeAppException("exec_shell", e);
        }
    }

}
