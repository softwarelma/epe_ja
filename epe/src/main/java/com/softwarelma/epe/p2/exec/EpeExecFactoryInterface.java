package com.softwarelma.epe.p2.exec;

import com.softwarelma.epe.p1.app.EpeAppException;

public interface EpeExecFactoryInterface {

    public boolean isFunc(String funcName) throws EpeAppException;

    public EpeExecInterface getNewFuncInstance(String funcName) throws EpeAppException;

    public String getClassNamePrefix();

}
