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

        if (args.length == 0) {
            programPath = EpeAppConstants.PROGRAM_DEFAULT_PATH;
            this.start(globalParams, programPath);
        } else if (args.length == 2 && "-f".equals(args[0])) {
            for (int i = 1; i < args.length; i++) {
                programPath = args[1];
                this.start(globalParams, programPath);
            }
        } else {
            List<String> list = Arrays.asList(args);
            throw new EpeAppException("Invalid args: " + list);
        }
    }

    private void start(EpeAppGlobalParams globalParams, String programPath) throws EpeAppException {
        EpeAppUtils.checkNull("programPath", programPath);
        String progName = "\"" + programPath + "\"";
        StringBuilder step = new StringBuilder("retrieving the PROG " + progName);

        try {
            // DbpAppLogger.log(step);
            Map<String, String> mapNotContainedReplaced = new HashMap<>();
            EpeProgInterface prog;

            prog = EpeProgFactory.getInstanceFromProgramPath(programPath, mapNotContainedReplaced);
            // prog = EpeProgFactory.getInstanceFromProgramContent(programContent, mapNotContainedReplaced);

            String step0 = "executing from prog " + progName + " the SENTENCE ";
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
