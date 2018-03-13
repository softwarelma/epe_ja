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
import com.softwarelma.epe.p3.print.EpePrintFinalPrint_default_exec_file_name;
import com.softwarelma.epe.p3.print.EpePrintFinalPrint_os_command;
import com.softwarelma.epe.p3.print.EpePrintFinalPrint_wrapped_command;

public final class EpeGenericFinalExec extends EpeGenericAbstract {

    enum WRAP_TYPE {
        wrap, nowrap, file
    }

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "exec, expected the command, the wrapping option (wrap|nowrap) and optionally the "
                + "file name and the encoding of the file.";
        String command = this.getStringAt(listExecResult, 0, postMessage);
        String wrapStr = this.getStringAt(listExecResult, 1, postMessage);
        EpeAppUtils.checkContains(new String[] { "wrap", "nowrap", "file" }, "wrapping option", wrapStr);
        WRAP_TYPE wrapType = WRAP_TYPE.valueOf(wrapStr);
        // boolean wrap = wrapStr.equals("wrap");
        String execFilename = listExecResult.size() > 2 ? this.getStringAt(listExecResult, 2, postMessage)
                : EpePrintFinalPrint_default_exec_file_name.retrieveDefaultExecFilename();
        String encoding = listExecResult.size() > 3 ? this.getStringAt(listExecResult, 3, postMessage)
                : EpeAppConstants.ENCODING_DEFAULT;
        boolean doLog = execParams.getGlobalParams().isPrintToConsole();
        Map.Entry<Integer, String> exitAndOutput;

        switch (wrapType) {
        case wrap:
            String wrappedCommand = EpePrintFinalPrint_wrapped_command.retrieveWrappedCommand(command);
            exitAndOutput = execWrappedCommand(doLog, wrappedCommand, execFilename, encoding);
            break;
        case nowrap:
            exitAndOutput = execCommand(doLog, command);
            break;
        case file:
            wrappedCommand = command;
            exitAndOutput = execWrappedCommand(doLog, wrappedCommand, execFilename, encoding);
            break;
        default:
            throw new EpeAppException("Unknown wrapping type: " + wrapType);
        }

        String errorCode = exitAndOutput.getKey().toString().equals("0") ? " (no error)" : " (error)";
        String logStr = "Command exit-value error-code: " + exitAndOutput.getKey() + errorCode;
        String str = exitAndOutput.getValue();
        logStr += "\nCommand output:\n" + str;
        this.log(execParams, logStr);
        return this.createResult(str);
    }

    public static Map.Entry<Integer, String> execCommand(boolean doLog, String command) throws EpeAppException {
        StringBuilder output = new StringBuilder();
        int exitVal;

        if (doLog) {
            EpeAppLogger.log("Executing command: " + command);
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(command.split(" "));
            pb.redirectErrorStream(true);
            Process p = pb.start();
            // Process p = Runtime.getRuntime().exec(command);

            exitVal = p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            reader.close();
        } catch (Exception e) {
            throw new EpeAppException("executeCommand(): " + command, e);
        }

        return new AbstractMap.SimpleEntry<>(exitVal, output.toString());
    }

    public static Map.Entry<Integer, String> execWrappedCommand(boolean doLog, String wrappedCommand,
            String execFilename, String encoding) throws EpeAppException {
        return execWrappedCommand(doLog, wrappedCommand, execFilename, encoding, false);
    }

    public static Map.Entry<Integer, String> execWrappedCommand(boolean doLog, String wrappedCommand,
            String execFilename, String encoding, boolean shellContext) throws EpeAppException {
        wrappedCommand = shellContext ? "#!/bin/sh\n\n" + wrappedCommand : wrappedCommand;

        if (doLog)
            EpeAppLogger.log("echo \"" + wrappedCommand.replace("\"", "\\\"") + "\" > " + execFilename);

        try {
            EpeEncodings ela = new EpeEncodings();
            ela.write(wrappedCommand, execFilename, encoding, false);
        } catch (Exception e) {
            throw new EpeAppException("mainForTarget", e);
        }

        String osCommand = EpePrintFinalPrint_os_command.retrieveExecOSCommand(execFilename);
        Map.Entry<Integer, String> exitAndOutput = execCommand(doLog, osCommand);
        return exitAndOutput;
    }

    // TODO ?
    public static Map.Entry<Integer, String> execFileCommand(boolean doLog, String command, String execFilename,
            String encoding) throws EpeAppException {
        return null;
    }

}
