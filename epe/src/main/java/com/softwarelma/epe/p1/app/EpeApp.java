package com.softwarelma.epe.p1.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p2.exec.EpeExec;
import com.softwarelma.epe.p2.prog.EpeProgFactory;
import com.softwarelma.epe.p2.prog.EpeProgInterface;
import com.softwarelma.epe.p2.prog.EpeProgSentInterface;

public final class EpeApp {

    public void start(String[] args) throws EpeAppException {
        EpeAppGlobalParams globalParams = new EpeAppGlobalParams();
        String programPath = null;

        if (args == null || args.length == 0) {
            programPath = EpeAppConstants.PROGRAM_DEFAULT_PATH;
            String progName = "\"" + programPath + "\"";
            this.start(globalParams, progName, programPath, true);
        } else if (args.length == 3 && "-p".equals(args[0])) {
            this.start(globalParams, args[1], args[2], false);
        } else if (args.length >= 2 && "-f".equals(args[0])) {
            for (int i = 1; i < args.length; i++) {
                programPath = args[i];
                globalParams.setPrintToConsole(false);
                globalParams.setSentIndex(0);
                String progName = "\"" + programPath + "\"";
                this.start(globalParams, progName, programPath, true);
            }
        } else {
            List<String> list = Arrays.asList(args);
            throw new EpeAppException("Invalid args: " + list + ". Expected nothing for default program ("
                    + EpeAppConstants.PROGRAM_DEFAULT_PATH
                    + ") or -p programName programContent or -f programPath1 ... programPathN "
                    + "with at least 1 program path.");
        }
    }

    /**
     * @param path
     *            true=path, false=content
     */
    private void start(EpeAppGlobalParams globalParams, String programName, String programPathOrContent, boolean path)
            throws EpeAppException {
        EpeAppUtils.checkNull("programPathOrContent", programPathOrContent);
        StringBuilder step = new StringBuilder("retrieving the PROGRAM " + programName);

        try {
            // DbpAppLogger.log(step);
            Map<String, String> mapNotContainedReplaced = new HashMap<>();
            EpeProgInterface prog;

            if (path) {
                prog = EpeProgFactory.getInstanceFromProgramPath(programPathOrContent, mapNotContainedReplaced);
            } else {
                prog = EpeProgFactory.getInstanceFromProgramContent(programPathOrContent, mapNotContainedReplaced);
            }

            String step0 = "executing from program " + programName + " the SENTENCE ";
            EpeExec exec = new EpeExec();

            this.executeLabels(globalParams, exec, prog, step, step0, mapNotContainedReplaced);
            this.executeSents(globalParams, exec, prog, step, step0, mapNotContainedReplaced);
        } catch (Exception e) {
            if (!(e instanceof EpeAppException)) {
                new EpeAppException("DbpApp.start() while " + step, e);
            }

            System.exit(0);
        }
    }

    private void executeLabels(EpeAppGlobalParams globalParams, EpeExec exec, EpeProgInterface prog, StringBuilder step,
            String step0, Map<String, String> mapNotContainedReplaced) throws EpeAppException {
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

    private void executeSents(EpeAppGlobalParams globalParams, EpeExec exec, EpeProgInterface prog, StringBuilder step,
            String step0, Map<String, String> mapNotContainedReplaced) throws EpeAppException {
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
            exec.execute(globalParams, progSent, mapNotContainedReplaced);

            if (progSent.getLiteralOrFuncName().equals("goto")) {
                i = globalParams.getSentIndex();
                EpeAppUtils.checkRange(i, 0, prog.size(), false, true, "EpeApp.start().");
            }
        }
    }

}
