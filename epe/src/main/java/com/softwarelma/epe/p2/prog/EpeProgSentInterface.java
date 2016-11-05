package com.softwarelma.epe.p2.prog;

import com.softwarelma.epe.p1.app.EpeAppConstants.SENT_TYPE;
import com.softwarelma.epe.p1.app.EpeAppException;

public interface EpeProgSentInterface {

    public SENT_TYPE getType();

    public String getLeftSideVarName();

    public String getLiteralOrFuncName();

    public int size();

    public EpeProgSentInterface get(int index) throws EpeAppException;

}
