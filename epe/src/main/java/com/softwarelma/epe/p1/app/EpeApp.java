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
        String step = "retrieving the PROG " + progName;

        try {
            // DbpAppLogger.log(step);
            Map<String, String> mapNotContainedReplaced = new HashMap<>();
            EpeProgInterface prog;

            prog = EpeProgFactory.getInstanceFromProgramPath(programPath, mapNotContainedReplaced);
            // prog = EpeProgFactory.getInstanceFromProgramContent(programContent, mapNotContainedReplaced);

            String step0 = "executing from prog " + progName + " the SENTENCE ";
            EpeProgSentInterface progSent;
            EpeExec exec = new EpeExec();

            for (int i = 0; i < prog.size(); i++) {
                progSent = prog.get(i);
                globalParams.setSentIndex(i);
                step = step0 + (i + 1) + "/" + prog.size() + " 1-based: " + progSent.toString();
                // DbpAppLogger.log(step);
                // EpeExecResult execResult =
                exec.execute(globalParams, progSent, mapNotContainedReplaced);

                if (progSent.getLiteralOrFuncName().equals("goto")) {
                    i = globalParams.getSentIndex();
                    EpeAppUtils.checkRange(i, 0, prog.size(), false, true, "EpeApp.start().");
                }
            }
        } catch (Exception e) {
            if (!(e instanceof EpeAppException)) {
                new EpeAppException("DbpApp.start() while " + step, e);
            }

            System.exit(0);
        }
    }
}
