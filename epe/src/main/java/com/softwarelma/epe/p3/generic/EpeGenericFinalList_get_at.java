package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecFactoryInterface;
import com.softwarelma.epe.p2.exec.EpeExecInterface;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p2.pack.EpePackGenericFactory;

public final class EpeGenericFinalList_get_at extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_get_at, expected the list (list of str or list of list), the index and optionally "
                + "the default element to return in case of no element at specified index.";
        // String index = this.getStringAt(listExecResult, 1, postMessage);
        // int ind = EpeAppUtils.parseInt(index);
        EpeExecFactoryInterface factory = new EpePackGenericFactory();
        EpeExecInterface func;

        if (this.isStringAt(listExecResult, 0, postMessage)) {
            this.getListStringAt(listExecResult, 0, postMessage);// throwing
            return null;// unreachable
        }

        if (this.isListStringAt(listExecResult, 0, postMessage)) {
            func = factory.getNewFuncInstance("list_get_str_at");
        } else if (this.isListListStringAt(listExecResult, 0, postMessage)) {
            func = factory.getNewFuncInstance("list_get_list_at");
        } else {
            throw new EpeAppException(postMessage);
        }

        // TODO doFunc to public static
        return func.doFunc(execParams, listExecResult);
    }

}
