package com.softwarelma.epe.p2.exec;

public class EpeExecResult {

    private EpeExecContent execContent;

    @Override
    public String toString() {
        return execContent + "";
    }

    public EpeExecContent getExecContent() {
        return execContent;
    }

    public void setExecContent(EpeExecContent execContent) {
        this.execContent = execContent;
    }

}
