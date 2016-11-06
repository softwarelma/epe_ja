package com.softwarelma.epe.p2.exec;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;

public interface EpeExecInterface {

    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException;

}
