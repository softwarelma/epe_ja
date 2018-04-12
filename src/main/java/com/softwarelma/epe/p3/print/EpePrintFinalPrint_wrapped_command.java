package com.softwarelma.epe.p3.print;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.generic.EpeGenericFinalIs_mac;
import com.softwarelma.epe.p3.generic.EpeGenericFinalIs_solaris;
import com.softwarelma.epe.p3.generic.EpeGenericFinalIs_unix;
import com.softwarelma.epe.p3.generic.EpeGenericFinalIs_windows;

public final class EpePrintFinalPrint_wrapped_command extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "print_wrapped_command, expected the command to wrap.";
        String command = getStringAt(listExecResult, 0, postMessage);
        String str = retrieveWrappedCommand(command);
        log(execParams, str);
        return createResult(str);
    }

    public static String retrieveWrappedCommand(String command) throws EpeAppException {
        return retrieveNewTerminalCommandPrefix() + command + retrieveNewTerminalCommandSuffix();
    }

    private static String retrieveNewTerminalCommandPrefix() throws EpeAppException {
        String newTerminalCommandPrefix = null;

        if (EpeGenericFinalIs_windows.isWindows()) {
            newTerminalCommandPrefix = EpeAppConstants.EXEC_NEW_TERM_COMMAND_PREFIX_WIN;
        } else if (EpeGenericFinalIs_unix.isUnix()) {
            newTerminalCommandPrefix = EpeAppConstants.EXEC_NEW_TERM_COMMAND_PREFIX_LIN;
        } else if (EpeGenericFinalIs_mac.isMac()) {
            // ?
        } else if (EpeGenericFinalIs_solaris.isSolaris()) {
            // ?
        }

        EpeAppUtils.checkNull("newTerminalCommandPrefix (OS: " + EpePrintFinalPrint_os_name.retrieveOsName() + ")",
                newTerminalCommandPrefix);
        return newTerminalCommandPrefix;
    }

    private static String retrieveNewTerminalCommandSuffix() throws EpeAppException {
        String newTerminalCommandSuffix = null;

        if (EpeGenericFinalIs_windows.isWindows()) {
            newTerminalCommandSuffix = EpeAppConstants.EXEC_NEW_TERM_COMMAND_SUFFIX_WIN;
        } else if (EpeGenericFinalIs_unix.isUnix()) {
            newTerminalCommandSuffix = EpeAppConstants.EXEC_NEW_TERM_COMMAND_SUFFIX_LIN;
        } else if (EpeGenericFinalIs_mac.isMac()) {
            // ?
        } else if (EpeGenericFinalIs_solaris.isSolaris()) {
            // ?
        }

        EpeAppUtils.checkNull("newTerminalCommandPrefix (OS: " + EpePrintFinalPrint_os_name.retrieveOsName() + ")",
                newTerminalCommandSuffix);
        return newTerminalCommandSuffix;
    }

}
