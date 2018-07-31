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
        String postMessage = "list_files_recursive, expected dir name and optionally one or more of the following params "
                + "in any order and respecting the keys of the properties:\n\"" + EpeDiskFinalList_files.PROP_PREFIX
                + "=some prefix\", " + "\"" + EpeDiskFinalList_files.PROP_CONTAINED + "=some contained string\", \""
                + EpeDiskFinalList_files.PROP_SUFFIX + "=some suffix\".";
        String dirName = getStringAt(listExecResult, 0, postMessage);
        String prefixContainedOrSuffix1 = getStringAt(listExecResult, 1, postMessage, null);
        String prefixContainedOrSuffix2 = getStringAt(listExecResult, 2, postMessage, null);
        String prefixContainedOrSuffix3 = getStringAt(listExecResult, 3, postMessage, null);
        List<String> listRet = listFilesRecursive(dirName, prefixContainedOrSuffix1, prefixContainedOrSuffix2,
                prefixContainedOrSuffix3);
        log(execParams, listRet);
        return createResult(listRet);
    }

    public static List<String> listFilesRecursive(String dirName) throws EpeAppException {
        return listFilesRecursive(dirName, null, null, null);
    }

    public static List<String> listFilesRecursive(String dirName, String prefixContainedOrSuffix1,
            String prefixContainedOrSuffix2, String prefixContainedOrSuffix3) throws EpeAppException {
        dirName = EpeAppUtils.cleanDirName(dirName);
        List<String> listDir = EpeDiskFinalList_dirs_recursive.listDirsRecursive(dirName);
        List<String> listRet = new ArrayList<>();
        String[] prefixContainedAndSuffix = EpeDiskFinalList_files.retrievePrefixContainedAndSuffix(
                prefixContainedOrSuffix1, prefixContainedOrSuffix2, prefixContainedOrSuffix3);

        for (String dir : listDir) {
            if (EpeDiskFinalList_files.isValidFileOrDir(dir, prefixContainedAndSuffix))
                listRet.add(dir);
            addOnlyFileChilds(dir, listRet, prefixContainedOrSuffix1, prefixContainedOrSuffix2,
                    prefixContainedOrSuffix3);
        }

        addOnlyFileChilds(dirName, listRet, prefixContainedOrSuffix1, prefixContainedOrSuffix2,
                prefixContainedOrSuffix3);
        return listRet;
    }

    public static void addOnlyFileChilds(String dirName, List<String> listRet) throws EpeAppException {
        addOnlyFileChilds(dirName, listRet, null, null, null);
    }

    public static void addOnlyFileChilds(String dirName, List<String> listRet, String prefixContainedOrSuffix1,
            String prefixContainedOrSuffix2, String prefixContainedOrSuffix3) throws EpeAppException {
        dirName = EpeAppUtils.cleanDirName(dirName);
        List<String> listFile = EpeDiskFinalList_files.listFiles(dirName, prefixContainedOrSuffix1,
                prefixContainedOrSuffix2, prefixContainedOrSuffix3);
        for (String file : listFile) {// relative paths
            file = dirName + file;// absolute path
            if (new File(file).isFile())
                listRet.add(file);
        }
    }

}
