package com.softwarelma.epe.p2.exec;

import java.util.List;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;

public class EpeExecContentInternal {

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

    public EpeExecContentInternal(List<List<String>> listListStr, String fake) {
        this.str = null;
        this.listStr = null;
        this.listListStr = listListStr;
    }

    @Override
    public String toString() {
        if (this.isString()) {
            return this.str;
        } else if (this.isListString()) {
            return this.listToString(this.listStr);
        } else {
            return this.listListToString(this.listListStr);
        }
    }

    private String listListToString(List<List<String>> listListStr) {
        StringBuilder sb = new StringBuilder();

        for (List<String> listStr : listListStr) {
            sb.append(this.listToString(listStr));
        }

        return sb.toString();
    }

    private String listToString(List<String> listStr) {
        StringBuilder sb = new StringBuilder();

        for (String str : listStr) {
            sb.append(str);
        }

        return sb.toString();
    }

    public boolean isString() {
        return this.str != null;
    }

    public boolean isListString() {
        return this.listStr != null;
    }

    public boolean isListListString() {
        return this.listListStr != null;
    }

    public String getStr() {
        return str;
    }

    public List<String> getListStr() {
        return listStr;
    }

    public List<List<String>> getListListStr() {
        return listListStr;
    }

    public DataSource getDataSource() throws EpeAppException {
        throw new EpeAppException("Method not valid");
    }

}
