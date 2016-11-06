package com.softwarelma.epe.p1.app;

public class EpeAppGlobalParams {

    private boolean printToConsole;

    @Override
    public String toString() {
        return printToConsole + "";
    }

    public boolean isPrintToConsole() {
        return printToConsole;
    }

    public void setPrintToConsole(boolean printToConsole) {
        this.printToConsole = printToConsole;
    }

}
