package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

/**
 * absolute names
 * 
 * @author ellison
 *
 */
public final class EpeDiskFinalList_files_recursive extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_files_recursive, expected dir name.";
        String dirName = getStringAt(listExecResult, 0, postMessage);
        List<String> listRet = listFilesRecursive(dirName);
        log(execParams, listRet);
        return createResult(listRet);
    }

    public static List<String> listFilesRecursive(String dirName) throws EpeAppException {
        dirName = EpeAppUtils.cleanDirName(dirName);
        List<String> listDir = EpeDiskFinalList_dirs_recursive.listDirsRecursive(dirName);
        List<String> listRet = new ArrayList<>();

        for (String dir : listDir) {
            listRet.add(dir);
            addOnlyFileChilds(dir, listRet);
        }

        addOnlyFileChilds(dirName, listRet);
        return listRet;
    }

    public static void addOnlyFileChilds(String dirName, List<String> listRet) throws EpeAppException {
        dirName = EpeAppUtils.cleanDirName(dirName);
        List<String> listFile = EpeDiskFinalList_files.listFiles(dirName);
        for (String file : listFile) {// relative paths
            file = dirName + file;// absolute path
            if (new File(file).isFile())
                listRet.add(file);
        }
    }

}
