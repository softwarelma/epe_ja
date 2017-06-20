package com.softwarelma.epe.p3.db;

import java.util.List;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;

public class EpeDbContentInternalDb extends EpeExecContentInternal {

    private final DataSource dataSource;
    private final List<EpeDbEntity> listEntity;

    public EpeDbContentInternalDb(String str) throws EpeAppException {
        super(str);
        this.dataSource = null;
        this.listEntity = null;
    }

    public EpeDbContentInternalDb(List<String> listStr, DataSource dataSource) throws EpeAppException {
        super(listStr);
        this.dataSource = dataSource;
        this.listEntity = null;
    }

    public EpeDbContentInternalDb(List<String> listStr, List<EpeDbEntity> listEntity) throws EpeAppException {
        super(listStr);
        this.dataSource = null;
        this.listEntity = listEntity;
    }

    public EpeDbContentInternalDb(List<List<String>> listListStr, String fake) throws EpeAppException {
        super(listListStr, fake);
        this.dataSource = null;
        this.listEntity = null;
    }

    @Override
    public DataSource getDataSource() throws EpeAppException {
        return dataSource;
    }

    @Override
    public List<EpeDbEntity> getListEntity() throws EpeAppException {
        return listEntity;
    }

}
