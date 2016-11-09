package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

/**
 * relatives names
 * 
 * @author ellison
 *
 */
public final class EpeDiskFinalList_dirs extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_dirs, expected dir name.";
        String dirName = this.getStringAt(listExecResult, 0, postMessage);
        File dir = new File(dirName);
        EpeAppUtils.checkDir(dir);
        List<String> list = this.listDirs(dir);
        this.log(execParams, list);
        return this.createResult(list);
    }

    private List<String> listDirs(File dir) {
        List<String> list = new ArrayList<>();

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                list.add(file.getName());
            }
        }

        return list;
    }

}
