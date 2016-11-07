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
        String programContent = null;

        if (args.length == 0) {
            programPath = EpeAppConstants.PROGRAM_DEFAULT_PATH;
        } else if (args.length == 2 && "-f".equals(args[0])) {
            programPath = args[1];
        } else if (args.length == 2 && "-p".equals(args[0])) {
            programContent = args[1];
        } else {
            List<String> list = Arrays.asList(args);
            throw new EpeAppException("Invalid args: " + list);
        }

        this.start(globalParams, programPath, programContent);
    }

    private void start(EpeAppGlobalParams globalParams, String programPath, String programContent) {
        String progName = programPath == null ? "CONTENT" : "\"" + programPath + "\"";
        String step = "retrieving the PROG " + progName;

        try {
            // DbpAppLogger.log(step);
            Map<String, String> mapNotContainedReplaced = new HashMap<>();
            EpeProgInterface prog;

            if (programPath != null) {
                prog = EpeProgFactory.getInstanceFromProgramPath(programPath, mapNotContainedReplaced);
            } else if (programContent != null) {
                prog = EpeProgFactory.getInstanceFromProgramContent(programContent, mapNotContainedReplaced);
            } else {
                throw new EpeAppException("Program not found");
            }

            String step0 = "executing from prog " + progName + " the SENTENCE ";
            EpeProgSentInterface progSent;
            EpeExec exec = new EpeExec();

            for (int i = 0; i < prog.size(); i++) {
                progSent = prog.get(i);
                step = step0 + (i + 1) + "/" + prog.size() + " 1-based: " + progSent.toString();
                // DbpAppLogger.log(step);
                // EpeExecResult execResult =
                exec.execute(globalParams, progSent, mapNotContainedReplaced);
            }
        } catch (Exception e) {
            if (!(e instanceof EpeAppException)) {
                new EpeAppException("DbpApp.start() while " + step, e);
            }

            System.exit(0);
        }
    }
}
