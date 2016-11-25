package com.softwarelma.epe.p2.exec;

import java.util.List;

public final class EpeExecContentInternal {

    private final String str;
    private final List<String> listStr;
    private final List<List<String>> listListStr;

    public EpeExecContentInternal(String str) {
        this.str = str;
        this.listStr = null;
        this.listListStr = null;
    }

    public EpeExecContentInternal(List<String> listStr) {
        this.str = null;
        this.listStr = listStr;
        this.listListStr = null;
    }

    public EpeExecContentInternal(List<List<String>> listListStr) {
        this.str = null;
        this.listStr = null;
        this.listListStr = listListStr;
    }

    @Override
    public String toString() {
        return listStr == null ? str : this.listToString();
    }

    public boolean isString() {
        return this.str != null;
    }

    public boolean isListString() {
        return this.listStr != null;
    }

    public String getStr() {
        return str;
    }

    public List<String> getListStr() {
        return listStr;
    }

    private String listToString() {
        StringBuilder sb = new StringBuilder();

        for (String str : this.listStr) {
            sb.append(str);
        }

        return sb.toString();
    }

}
