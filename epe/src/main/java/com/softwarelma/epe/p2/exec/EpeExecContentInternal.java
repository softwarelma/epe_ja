package com.softwarelma.epe.p2.exec;

import java.util.List;

public final class EpeExecContentInternal {

    private final String str;
    private final List<String> listStr;

    public EpeExecContentInternal(String str) {
        this.str = str;
        this.listStr = null;
    }

    public EpeExecContentInternal(List<String> listStr) {
        this.str = null;
        this.listStr = listStr;
    }

    @Override
    public String toString() {
        return listStr == null ? str : listStr.toString();
    }

    public String getStr() {
        return str;
    }

    public List<String> getListStr() {
        return listStr;
    }

}
