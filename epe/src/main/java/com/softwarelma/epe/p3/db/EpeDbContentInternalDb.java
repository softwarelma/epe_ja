package com.softwarelma.epe.p3.db;

import java.util.List;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;

public class EpeDbContentInternalDb extends EpeExecContentInternal {

    private final DataSource dataSource;
    private final EpeDbEntity entity;

    public EpeDbContentInternalDb(String str) throws EpeAppException {
        super(str);
        this.dataSource = null;
        this.entity = null;
    }

    public EpeDbContentInternalDb(List<String> listStr, DataSource dataSource) throws EpeAppException {
        super(listStr);
        this.dataSource = dataSource;
        this.entity = null;
    }

    public EpeDbContentInternalDb(List<String> listStr, EpeDbEntity entity) throws EpeAppException {
        super(listStr);
        this.dataSource = null;
        this.entity = entity;
    }

    public EpeDbContentInternalDb(List<List<String>> listListStr, String fake) throws EpeAppException {
        super(listListStr, fake);
        this.dataSource = null;
        this.entity = null;
    }

    @Override
    public DataSource getDataSource() throws EpeAppException {
        return dataSource;
    }

    @Override
    public EpeDbEntity getEntity() throws EpeAppException {
        return entity;
    }

}
