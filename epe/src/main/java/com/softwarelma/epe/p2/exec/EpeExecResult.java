package com.softwarelma.epe.p2.exec;

public class EpeExecResult {

    private final boolean printToConsole;
    private EpeExecContent execContent;

    public EpeExecResult(boolean printToConsole) {
        this.printToConsole = printToConsole;
    }

    @Override
    public String toString() {
        return "EpeExecResult [printToConsole=" + printToConsole + ", execContent=" + execContent + "]";
    }

    public boolean isPrintToConsole() {
        return printToConsole;
    }

    public EpeExecContent getExecContent() {
        return execContent;
    }

    public void setExecContent(EpeExecContent execContent) {
        this.execContent = execContent;
    }

}
