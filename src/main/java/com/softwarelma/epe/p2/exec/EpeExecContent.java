package com.softwarelma.epe.p2.exec;

import java.util.List;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;

/**
 * this class is intended to implement a NOP, so the contentInternal can be null
 */
public final class EpeExecContent {

    private final EpeExecContentInternal contentInternal;
    private boolean prop;

    @Override
    public String toString() {
        return contentInternal == null ? null : contentInternal.toString();
    }

    public String toString(String sepExternal, String sepInternal) {
        return contentInternal == null ? null : contentInternal.toString(sepExternal, sepInternal);
    }

    public String toString(String sepExternal, List<Integer> listWidth, String colSuffix) throws EpeAppException {
        return contentInternal == null ? null : contentInternal.toString(sepExternal, listWidth, colSuffix);
    }

    public boolean isNull() {
        return this.contentInternal == null;
    }

    public EpeExecContent(EpeExecContentInternal contentInternal) {
        this.contentInternal = contentInternal;
    }

    public EpeExecContentInternal getContentInternal() {
        return contentInternal;
    }

    public boolean isString() {
        return this.contentInternal != null && this.contentInternal.isString();
    }

    public boolean isListString() {
        return this.contentInternal != null && this.contentInternal.isListString();
    }

    public boolean isListListString() {
        return this.contentInternal != null && this.contentInternal.isListListString();
    }

    public String getStr() {
        return this.contentInternal == null ? null : this.contentInternal.getStr();
    }

    public List<String> getListStr() {
        return this.contentInternal == null ? null : this.contentInternal.getListStr();
    }

    public List<List<String>> getListListStr() {
        return this.contentInternal == null ? null : this.contentInternal.getListListStr();
    }

    public DataSource getDataSource() throws EpeAppException {
        return this.contentInternal == null ? null : this.contentInternal.getDataSource();
    }

    public boolean isProp() {
        return prop;
    }

    public void setProp(boolean prop) {
        this.prop = prop;
    }

}
