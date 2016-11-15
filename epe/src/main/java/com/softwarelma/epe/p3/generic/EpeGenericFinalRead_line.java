package com.softwarelma.epe.p3.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalRead_line extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "read_line, expected the screen message, the alternatives (also empty but not null), "
                + "the required bool and the case sensitive bool.";
        String screenMessage = this.getStringAt(listExecResult, 0, postMessage);
        String alternatives = this.getStringAt(listExecResult, 1, postMessage);
        String requiredStr = this.getStringAt(listExecResult, 2, postMessage);
        boolean required = EpeAppUtils.parseBoolean(requiredStr);
        String caseSensitiveStr = this.getStringAt(listExecResult, 3, postMessage);
        boolean caseSensitive = EpeAppUtils.parseBoolean(caseSensitiveStr);
        String str = this.retrieveExternalInput(execParams.getGlobalParams().isPrintToConsole(), screenMessage,
                alternatives, required, caseSensitive);
        this.log(execParams, str);
        return this.createResult(str);
    }

    private String retrieveExternalInput(boolean doLog, String screenMessage, String alternatives, boolean required,
            boolean caseSensitive) throws EpeAppException {
        EpeAppUtils.checkNull("screenMessage", screenMessage);
        EpeAppUtils.checkNull("alternatives", alternatives);
        screenMessage = screenMessage.startsWith("tcm_") ? screenMessage.substring(4) : screenMessage;
        screenMessage = screenMessage.replace("_", " ");
        String input = null;

        if (alternatives.equals("*")) {
            input = this.retrieveConsoleInput(doLog, screenMessage);
            return input;
        }

        // if (alternatives.equalsIgnoreCase("si")) {
        // screenMessage += " (si/no)";
        // }

        if (alternatives.startsWith("*")) {
            return alternatives.substring(1);
        }

        String matchedValue;
        String[] possibleValue = alternatives.split("\\|");
        String alternativesMessage = possibleValue.length == 1 ? "" : ": [" + alternatives + "]";
        screenMessage += alternativesMessage;

        if (EpeAppUtils.isEmptyArray(possibleValue)) {
            if (required) {
                while (EpeAppUtils.isEmptyTrimming(input)) {
                    input = this.retrieveConsoleInput(doLog, screenMessage);
                }

                return input;
            } else {
                input = this.retrieveConsoleInput(doLog, screenMessage);
                return input;
            }
        } else {
            while (true) {
                input = this.retrieveConsoleInput(doLog, screenMessage);
                matchedValue = retrieveMatchedValue(input, possibleValue, caseSensitive);

                if (matchedValue != null) {
                    return matchedValue;
                }
            }
        }
    }

    private String retrieveConsoleInput(boolean doLog, String screenMessage) throws EpeAppException {
        EpeAppLogger.log(screenMessage);
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {
            String line = bufferRead.readLine();

            if (doLog) {
                EpeAppLogger.log(line);
            }

            return line;
        } catch (IOException e) {
            throw new EpeAppException("Retrieving the console input", e);
        }
    }

    private String retrieveMatchedValue(String value, String[] possibleValue, boolean caseSensitive) {
        value = value == null ? "" : value;

        for (int i = 0; i < possibleValue.length; i++) {
            if (!caseSensitive && value.equalsIgnoreCase(possibleValue[i])) {
                return possibleValue[i];
            } else if (caseSensitive && value.equals(possibleValue[i])) {
                return possibleValue[i];
            }
        }

        return null;
    }

}
