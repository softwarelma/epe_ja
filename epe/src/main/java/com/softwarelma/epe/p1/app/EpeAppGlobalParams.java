package com.softwarelma.epe.p1.app;

public class EpeAppGlobalParams {

    private boolean printToConsole;
    private int sentIndex;

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

    public int getSentIndex() {
        return sentIndex;
    }

    public void setSentIndex(int sentIndex) {
        this.sentIndex = sentIndex;
    }

}
