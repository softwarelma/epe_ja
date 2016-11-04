package com.softwarelma.epe.p1.app;

public abstract class EpeAppLogger {

    public static void log(EpeAppException e) {
        logSystemOutPrintln(e.getMessage());
        e.printStackTrace();

        if (e.getCause() != null) {
            logSystemOutPrintln(e.getCause().getMessage());
            e.getCause().printStackTrace();
        }
    }

    public static void log(String text) {
        logSystemOutPrintln(text);
    }

    public static void logSystemOutPrintln(String text) {
        System.out.println(text);
    }

}
