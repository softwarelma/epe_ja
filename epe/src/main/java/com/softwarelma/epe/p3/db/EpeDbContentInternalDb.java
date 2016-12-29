package com.softwarelma.epe.p3.db;

import java.util.List;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;

public class EpeDbContentInternalDb extends EpeExecContentInternal {

    private DataSource dataSource;

    public EpeDbContentInternalDb(String str) throws EpeAppException {
        super(str);
    }

    public EpeDbContentInternalDb(List<String> listStr) throws EpeAppException {
        super(listStr);
    }

    public EpeDbContentInternalDb(List<List<String>> listListStr, String fake) throws EpeAppException {
        super(listListStr, fake);
    }

    @Override
    public DataSource getDataSource() throws EpeAppException {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
