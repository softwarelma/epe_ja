package com.softwarelma.epe.p1.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p2.exec.EpeExec;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p2.prog.EpeProgFactory;
import com.softwarelma.epe.p2.prog.EpeProgInterface;
import com.softwarelma.epe.p2.prog.EpeProgSentInterface;

class EpeAppProgram {

    private boolean path;
    private String programName;
    private String programPathOrContent;

    public EpeAppProgram(boolean path, String programName, String programPathOrContent) throws EpeAppException {
        super();
        EpeAppUtils.checkEmpty("programName", programName);
        EpeAppUtils.checkEmpty("programPathOrContent", programPathOrContent);
        this.path = path;
        this.programName = programName;
        this.programPathOrContent = programPathOrContent;
    }

    @Override
    public String toString() {
        return "EpeAppProgram [path=" + path + ", programName=" + programName + ", programPathOrContent="
                + programPathOrContent + "]";
    }

    public boolean isPath() {
        return path;
    }

    public String getProgramName() {
        return programName;
    }

    public String getProgramPath() throws EpeAppException {
        if (!this.path) {
            throw new EpeAppException("The program is content-based, it has no path.");
        }

        return programPathOrContent;
    }

    public String getProgramContent() throws EpeAppException {
        if (this.path) {
            throw new EpeAppException("The program is path-based, it has no content.");
        }

        return programPathOrContent;
    }

}

public final class EpeApp {

    public EpeExecResult start(String[] args) throws EpeAppException {
        EpeAppGlobalParams globalParams = new EpeAppGlobalParams();
        String programPath = null;
        EpeAppProgram program;
        List<EpeAppProgram> listProgram = new ArrayList<>();

        if (args == null || args.length == 0) {
            programPath = EpeAppConstants.PROGRAM_DEFAULT_PATH;
            String progName = "\"" + programPath + "\"";
            program = new EpeAppProgram(true, progName, programPath);
            return this.startByProgram(globalParams, program);
        }

        String exceptionMessage = this.retrieveExceptionMessage(args);
        boolean path = this.isPath(args[0], exceptionMessage);
        String arg;
        String programName = null;

        for (int i = 1; i < args.length; i++) {
            arg = args[i];

            if (this.isNewArg(arg)) {
                if (programName != null) {
                    throw new EpeAppException(exceptionMessage);
                }

                path = this.isPath(arg, exceptionMessage);
                continue;
            }

            if (path) {
                if (programName != null) {
                    throw new EpeAppException(exceptionMessage);
                }

                programPath = arg;
                programName = "\"" + programPath + "\"";
                program = new EpeAppProgram(true, programName, programPath);
                programName = null;
                listProgram.add(program);
                continue;
            }

            // CONTENT

            // prog name
            if (programName == null) {
                programName = arg;
                continue;
            }

            // prog content
            String programConent = arg;
            program = new EpeAppProgram(false, programName, programConent);
            programName = null;
            listProgram.add(program);
        }

        EpeExecResult result = null;

        for (EpeAppProgram programI : listProgram) {
            result = this.startByProgram(globalParams, programI);
        }

        return result;
    }

    private String retrieveExceptionMessage(String[] args) throws EpeAppException {
        EpeAppUtils.checkNull("args", args);
        List<String> list = Arrays.asList(args);
        String exceptionMessage = "Invalid args: " + list + ". Expected nothing for default program ("
                + EpeAppConstants.PROGRAM_DEFAULT_PATH
                + ") or -p programName1 programContent1 ... programNameN programContentN or "
                + "-f programPath1 ... programPathN or a mixed approach (-p and -f).";
        return exceptionMessage;
    }

    private boolean isNewArg(String arg) {
        return "-p".equals(arg) || "-f".equals(arg);
    }

    private boolean isPath(String arg, String exceptionMessage) throws EpeAppException {
        if ("-p".equals(arg)) {
            return false;
        } else if ("-f".equals(arg)) {
            return true;
        } else {
            throw new EpeAppException(exceptionMessage);
        }
    }

    /**
     * @param path
     *            true means a program full file name, false means a program
     *            content
     */
    private EpeExecResult startByProgram(EpeAppGlobalParams globalParams, EpeAppProgram program)
            throws EpeAppException {
        EpeAppUtils.checkNull("globalParams", globalParams);
        EpeAppUtils.checkNull("program", program);
        globalParams.setPrintToConsole(false);
        globalParams.setSentIndex(0);
        StringBuilder step = new StringBuilder("retrieving the PROGRAM " + program.getProgramName());
        EpeExecResult result = null;

        try {
            // DbpAppLogger.log(step);
            Map<String, String> mapNotContainedReplaced = new HashMap<>();
            EpeProgInterface prog;

            if (program.isPath()) {
                prog = EpeProgFactory.getInstanceFromProgramPath(program.getProgramPath(), mapNotContainedReplaced);
            } else {
                prog = EpeProgFactory.getInstanceFromProgramContent(program.getProgramContent(),
                        mapNotContainedReplaced);
            }

            String step0 = "executing from program " + program.getProgramName() + " the SENTENCE ";
            EpeExec exec = new EpeExec();

            this.executeLabels(globalParams, exec, prog, step, step0, mapNotContainedReplaced);
            result = this.executeSents(globalParams, exec, prog, step, step0, mapNotContainedReplaced);
        } catch (Exception e) {
            if (!(e instanceof EpeAppException)) {
                new EpeAppException("DbpApp.start() while " + step, e);
            }

            System.exit(0);
        }

        return result;
    }

    private void executeLabels(EpeAppGlobalParams globalParams, EpeExec exec, EpeProgInterface prog, StringBuilder step,
            String step0, Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeAppUtils.checkNull("globalParams", globalParams);
        EpeProgSentInterface progSent;

        for (int i = 0; i < prog.size(); i++) {
            progSent = prog.get(i);
            globalParams.setSentIndex(i);

            step.delete(0, step.length());
            step.append(step0);
            step.append((i + 1));
            step.append("/");
            step.append(prog.size());
            step.append(" 1-based: ");
            step.append(progSent.toString());

            if (progSent.getLiteralOrFuncName().equals("label")) {
                // DbpAppLogger.log(step);
                // EpeExecResult execResult =
                exec.execute(globalParams, progSent, mapNotContainedReplaced);
            }
        }
    }

    private EpeExecResult executeSents(EpeAppGlobalParams globalParams, EpeExec exec, EpeProgInterface prog,
            StringBuilder step, String step0, Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeAppUtils.checkNull("globalParams", globalParams);
        EpeExecResult result = null;
        EpeProgSentInterface progSent;

        for (int i = 0; i < prog.size(); i++) {
            progSent = prog.get(i);
            globalParams.setSentIndex(i);

            step.delete(0, step.length());
            step.append(step0);
            step.append((i + 1));
            step.append("/");
            step.append(prog.size());
            step.append(" 1-based: ");
            step.append(progSent.toString());

            if (progSent.getLiteralOrFuncName().equals("label")) {
                continue;
            }

            // DbpAppLogger.log(step);
            // EpeExecResult execResult =
            result = exec.execute(globalParams, progSent, mapNotContainedReplaced);

            if (progSent.getLiteralOrFuncName().equals("goto")) {
                i = globalParams.getSentIndex();
                EpeAppUtils.checkRange(i, 0, prog.size(), false, true, "EpeApp.start().");
            }
        }

        return result;
    }

}
