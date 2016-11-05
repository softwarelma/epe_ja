package com.softwarelma.epe.p2.prog;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppConstants.SENT_TYPE;
import com.softwarelma.epe.p1.app.EpeAppException;

public final class EpeProgDefaultSent extends EpeProgAbstractSent {

    public EpeProgDefaultSent(SENT_TYPE type, String leftSideVarName, String literalOrFuncName,
            List<EpeProgSentInterface> listParam) throws EpeAppException {
        super(type, leftSideVarName, literalOrFuncName, listParam);
    }

}
