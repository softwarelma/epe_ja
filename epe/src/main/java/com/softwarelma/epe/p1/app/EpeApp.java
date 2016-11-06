package com.softwarelma.epe.p1.app;

import java.util.HashMap;
import java.util.Map;

import com.softwarelma.epe.p2.exec.EpeExec;
import com.softwarelma.epe.p2.prog.EpeProgFactory;
import com.softwarelma.epe.p2.prog.EpeProgInterface;
import com.softwarelma.epe.p2.prog.EpeProgSentInterface;

public final class EpeApp {

    public void start(String[] args) {
        EpeAppGlobalParams globalParams = new EpeAppGlobalParams();
        String arg = args != null && args.length > 0 ? args[0] : null;
        this.start(globalParams, arg);
    }

    public void start(EpeAppGlobalParams globalParams, String arg) {
        String programDefaultPath = EpeAppConstants.PROGRAM_DEFAULT_PATH;
        String step = "retrieving the PROG " + programDefaultPath + "";

        try {
            EpeAppUtils.checkNull("programDefaultPath", programDefaultPath);
            // DbpAppLogger.log(step);
            Map<String, String> mapNotContainedReplaced = new HashMap<>();
            EpeProgInterface prog = EpeProgFactory.getInstance(programDefaultPath, mapNotContainedReplaced);
            String step0 = "executing from prog " + programDefaultPath + " the SENTENCE ";
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
