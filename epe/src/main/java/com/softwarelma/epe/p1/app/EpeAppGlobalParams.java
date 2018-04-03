package com.softwarelma.epe.p1.app;

import java.util.HashMap;
import java.util.Map;

import com.softwarelma.epe.p2.exec.EpeExecContent;

public class EpeAppGlobalParams {

    private final Map<String, EpeExecContent> mapVarNameExecContent = new HashMap<>();
    private final Map<String, String> mapComments = new HashMap<>();
    private boolean printToConsole;
    private int sentIndex;

    @Override
    public String toString() {
        return "[printToConsole=" + printToConsole + ", sentIndex=" + sentIndex + "]";
    }

    public Map<String, EpeExecContent> getMapVarNameExecContent() {
        return mapVarNameExecContent;
    }

    public Map<String, String> getMapComments() {
        return mapComments;
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
