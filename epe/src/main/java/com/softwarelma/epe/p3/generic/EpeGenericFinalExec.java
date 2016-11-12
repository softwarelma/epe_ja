package com.softwarelma.epe.p3.generic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalExec extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "exec, expected the command, the wrapping option (wrap|nowrap) and optionally the "
                + "file name and the encoding of the file.";
        String command = this.getStringAt(listExecResult, 0, postMessage);
        String wrapStr = this.getStringAt(listExecResult, 1, postMessage);
        EpeAppUtils.checkContains(new String[] { "wrap", "nowrap" }, "wrapping option", wrapStr);
        boolean wrap = wrapStr.equals("wrap");
        String execFilename = listExecResult.size() > 2 ? this.getStringAt(listExecResult, 2, postMessage)
                : EpeGenericFinalPrint_default_exec_file_name.retrieveDefaultExecFilename();
        String encoding = listExecResult.size() > 3 ? this.getStringAt(listExecResult, 3, postMessage)
                : EpeAppConstants.ENCODING_DEFAULT;
        Map.Entry<Integer, String> exitAndOutput;

        if (wrap) {
            String wrappedCommand = EpeGenericFinalPrint_wrapped_command.retrieveWrappedCommand(command);
            exitAndOutput = execWrappedCommand(execParams.getGlobalParams().isPrintToConsole(), wrappedCommand,
                    execFilename, encoding);
        } else {
            exitAndOutput = execCommand(execParams.getGlobalParams().isPrintToConsole(), command);
        }

        String errorCode = exitAndOutput.getKey().toString().equals("0") ? " (no error)" : " (error)";
        String logStr = "Command exit-value error-code: " + exitAndOutput.getKey() + errorCode;
        String str = exitAndOutput.getValue();
        logStr += "\nCommand output:\n" + str;
        this.log(execParams, logStr);
        return this.createResult(str);
    }

    public static Map.Entry<Integer, String> execCommand(boolean doLog, String command) throws EpeAppException {
        StringBuffer output = new StringBuffer();
        int exitVal;

        if (doLog) {
            EpeAppLogger.log("Executing command: " + command);
        }

        try {
            Process p = Runtime.getRuntime().exec(command);
            exitVal = p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            throw new EpeAppException("executeCommand(): " + command, e);
        }

        return new AbstractMap.SimpleEntry<>(exitVal, output.toString());
    }

    public static Map.Entry<Integer, String> execWrappedCommand(boolean doLog, String wrappedCommand,
            String execFilename, String encoding) throws EpeAppException {
        if (doLog) {
            EpeAppLogger.log("echo \"" + wrappedCommand.replace("\"", "\\\"") + "\" > " + execFilename);
        }

        try {
            EpeEncodings ela = new EpeEncodings();
            ela.write(wrappedCommand, execFilename, encoding, false);
        } catch (Exception e) {
            throw new EpeAppException("mainForTarget", e);
        }

        String osCommand = EpeGenericFinalPrint_os_command.retrieveExecOSCommand(execFilename);
        Map.Entry<Integer, String> exitAndOutput = execCommand(doLog, osCommand);
        return exitAndOutput;
    }

}
