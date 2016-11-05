package com.softwarelma.epe.p2.exec;

public final class EpeExecParams {

    private final boolean printToConsole;

    @Override
    public String toString() {
        return printToConsole + "";
    }

    public EpeExecParams(boolean printToConsole) {
        this.printToConsole = printToConsole;
    }

    public boolean isPrintToConsole() {
        return printToConsole;
    }

}
