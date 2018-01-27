package com.softwarelma.epe.p0.main;

import com.softwarelma.epe.p1.app.EpeApp;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public class EpeMain {

    public static void main(String[] args) {
        start(args);
    }

    public static EpeExecResult start(String[] args) {
        EpeExecResult result = null;

        try {
            result = new EpeApp().start(args);
        } catch (Exception e) {
            if (!(e instanceof EpeAppException)) {
                new EpeAppException("main", e);
            }

            System.exit(0);
        }

        return result;
    }

}
