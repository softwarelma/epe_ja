package com.softwarelma.epe.p2.exec;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppGlobalParams;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeExecParams {

    private final EpeAppGlobalParams globalParams;

    public EpeExecParams(EpeAppGlobalParams globalParams) throws EpeAppException {
        EpeAppUtils.checkNull("globalParams", globalParams);
        this.globalParams = globalParams;
    }

    @Override
    public String toString() {
        return globalParams.toString();
    }

    public EpeAppGlobalParams getGlobalParams() {
        return globalParams;
    }

}
