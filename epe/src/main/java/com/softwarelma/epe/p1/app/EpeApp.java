package com.softwarelma.epe.p1.app;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p2.exec.EpeExec;
import com.softwarelma.epe.p2.exec.EpeExecDefaultSent;
import com.softwarelma.epe.p2.exec.EpeExecSentInterface;
import com.softwarelma.epe.p2.prog.EpeProgFactory;
import com.softwarelma.epe.p2.prog.EpeProgInterface;
import com.softwarelma.epe.p2.prog.EpeProgSentInterface;

public final class EpeApp {

    public void start(String[] args) {
        String arg = args != null && args.length > 0 ? args[0] : null;
        this.start(arg);
    }

    public void start(String arg) {
        // FIXME fake program
        arg = "progs/program.txt";

        String step = "retrieving the PROG " + arg + "";

        try {
            EpeAppUtils.checkNull("arg", arg);
            // DbpAppLogger.log(step);
            EpeProgInterface prog = EpeProgFactory.getInstance(arg);
            String step0 = "executing from prog " + arg + " the SENTENCE ";
            EpeProgSentInterface progSent;
            EpeExec exec = new EpeExec();

            for (int i = 0; i < prog.size(); i++) {
                progSent = prog.get(i);
                step = step0 + (i + 1) + "/" + prog.size() + " 1-based: " + progSent.toString();
                // DbpAppLogger.log(step);
                EpeExecSentInterface execSent = this.getExecSent(progSent);
                exec.execute(execSent);
            }
        } catch (Exception e) {
            if (!(e instanceof EpeAppException)) {
                new EpeAppException("DbpApp.start() while " + step, e);
            }

            System.exit(0);
        }
    }

    private EpeExecSentInterface getExecSent(EpeProgSentInterface progSent) throws EpeAppException {
        EpeAppUtils.checkNull("progSent", progSent);
        List<String> listParam = new ArrayList<>();

        for (int i = 0; i < progSent.size(); i++) {
            listParam.add(progSent.get(i));
        }

        EpeExecSentInterface execSent = new EpeExecDefaultSent(progSent.getVarName(), progSent.getLiteral(),
                progSent.getFuncName(), listParam);
        return execSent;
    }

}
