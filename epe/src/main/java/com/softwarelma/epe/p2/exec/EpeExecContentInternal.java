package com.softwarelma.epe.p2.exec;

import java.util.List;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public class EpeExecContentInternal {

    private final String str;
    private final List<String> listStr;
    private final List<List<String>> listListStr;

    public EpeExecContentInternal(String str) throws EpeAppException {
        EpeAppUtils.checkNull("str", str);
        this.str = str;
        this.listStr = null;
        this.listListStr = null;
    }

    public EpeExecContentInternal(List<String> listStr) throws EpeAppException {
        EpeAppUtils.checkNull("listStr", listStr);
        this.str = null;
        this.listStr = listStr;
        this.listListStr = null;
    }

    public EpeExecContentInternal(List<List<String>> listListStr, String fake) throws EpeAppException {
        EpeAppUtils.checkNull("listListStr", listListStr);
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
        } else if (this.isListString()) {
            return this.listListToString(this.listListStr);
        } else {
            throw new RuntimeException("Unknown internal content type");
        }
    }

    public String toString(String sepExternal, String sepInternal) {
        if (this.isString()) {
            return this.str;
        } else if (this.isListString()) {
            return this.listToString(this.listStr, sepInternal);
        } else {
            return this.listListToString(this.listListStr, sepExternal, sepInternal);
        }
    }

    public String toString(String sepExternal, List<Integer> listWidth, String suffixBackspace) throws EpeAppException {
        if (this.isString()) {
            return this.str;
        } else if (this.isListString()) {
            return this.listToString(this.listStr, listWidth, suffixBackspace);
        } else {
            return this.listListToString(this.listListStr, sepExternal, listWidth, suffixBackspace);
        }
    }

    private String listListToString(List<List<String>> listListStr) {
        StringBuilder sb = new StringBuilder();

        for (List<String> listStr : listListStr) {
            sb.append(this.listToString(listStr));
        }

        return sb.toString();
    }

    private String listListToString(List<List<String>> listListStr, String sepExternal, String sepInternal) {
        StringBuilder sb = new StringBuilder();
        String sepExternal2 = "";

        for (List<String> listStr : listListStr) {
            sb.append(sepExternal2);
            sepExternal2 = sepExternal;
            sb.append(this.listToString(listStr, sepInternal));
        }

        return sb.toString();
    }

    private String listListToString(List<List<String>> listListStr, String sepExternal, List<Integer> listWidth,
            String suffixBackspace) throws EpeAppException {
        StringBuilder sb = new StringBuilder();
        String sepExternal2 = "";

        for (List<String> listStr : listListStr) {
            sb.append(sepExternal2);
            sepExternal2 = sepExternal;
            sb.append(this.listToString(listStr, listWidth, suffixBackspace));
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

    private String listToString(List<String> listStr, String sepInternal) {
        StringBuilder sb = new StringBuilder();
        String sepInternal2 = "";

        for (String str : listStr) {
            sb.append(sepInternal2);
            sepInternal2 = sepInternal;
            sb.append(str);
        }

        return sb.toString();
    }

    private String listToString(List<String> listStr, List<Integer> listWidth, String suffixBackspace)
            throws EpeAppException {
        StringBuilder sb = new StringBuilder();
        String sepInternal2 = "";
        String str;
        int width;
        suffixBackspace = suffixBackspace == null ? "" : suffixBackspace;

        for (int i = 0; i < listStr.size(); i++) {
            str = listStr.get(i);
            sb.append(sepInternal2);
            width = this.retrieveWidth(listWidth, i);
            sepInternal2 = this.retrieveBackspaces(width, str) + suffixBackspace;
            sb.append(str);
        }

        return sb.toString();
    }

    private int retrieveWidth(List<Integer> listWidth, int i) throws EpeAppException {
        EpeAppUtils.checkNull("listWidth", listWidth);

        if (i < 0 || i > listWidth.size() - 1) {
            throw new EpeAppException("Index out bound " + i + " for list size " + listWidth.size());
        }

        return listWidth.get(i);

    }

    private String retrieveBackspaces(int width, String str) throws EpeAppException {
        StringBuilder sb = new StringBuilder(" ");
        str = str + "";// null becomes "null"

        if (str.length() > width) {
            throw new EpeAppException("The string (" + str + ") has a length (" + str.length() + ") bigger than "
                    + width);
        }

        for (int i = 0; i < width - str.length(); i++) {
            sb.append(" ");
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
