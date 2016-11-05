package com.softwarelma.epe.p2.prog;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppConstants.SENT_TYPE;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public abstract class EpeProgAbstractSent implements EpeProgSentInterface {

    private final SENT_TYPE type;
    private final String leftSideVarName;
    private final String literalOrFuncName;
    private final List<EpeProgSentInterface> listParam;

    public EpeProgAbstractSent(SENT_TYPE type, String leftSideVarName, String literalOrFuncName,
            List<EpeProgSentInterface> listParam) throws EpeAppException {
        super();
        EpeAppUtils.checkNull("type", type);
        EpeAppUtils.checkNull("literalOrFuncName", literalOrFuncName);

        this.type = type;
        this.leftSideVarName = leftSideVarName;
        this.literalOrFuncName = literalOrFuncName;

        if (listParam == null || listParam.isEmpty()) {
            this.listParam = new ArrayList<>();
        } else {
            this.listParam = new ArrayList<>(listParam);
        }
    }

    @Override
    public String toString() {
        String ret = this.leftSideVarName == null ? "" : this.leftSideVarName + " = ";

        if (!this.type.equals(SENT_TYPE.func)) {
            ret += this.literalOrFuncName;
            return ret;
        }

        String params = "";
        String sep = "";

        for (EpeProgSentInterface param : this.listParam) {
            params += sep + param;
            sep = ", ";
        }

        ret += this.literalOrFuncName + "(" + params + ")";
        return ret;
    }

    @Override
    public SENT_TYPE getType() {
        return type;
    }

    @Override
    public String getLeftSideVarName() {
        return leftSideVarName;
    }

    @Override
    public String getLiteralOrFuncName() {
        return literalOrFuncName;
    }

    @Override
    public int size() {
        return this.listParam.size();
    }

    @Override
    public EpeProgSentInterface get(int index) throws EpeAppException {
        try {
            return this.listParam.get(index);
        } catch (Exception e) {
            throw new EpeAppException("IndexOutOfBoundsException", e);
        }
    }

}
