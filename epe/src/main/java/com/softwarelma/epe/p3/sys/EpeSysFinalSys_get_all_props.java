package com.softwarelma.epe.p3.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeSysFinalSys_get_all_props extends EpeSysAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "sys_get_all_props.";
        List<String> listStr = new ArrayList<>();
        Properties props = System.getProperties();
        for (Object key : props.keySet())
            listStr.add(key + "=" + props.get(key));
        this.log(execParams, listStr);
        return this.createResult(listStr);
    }

}
