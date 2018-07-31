package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
public final class EpeDiskFinalList_files extends EpeDiskAbstract {

    public static final String PROP_PREFIX = "prefix";
    public static final String PROP_CONTAINED = "contained";
    public static final String PROP_SUFFIX = "suffix";

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_files, expected dir name and optionally one or more of the following params "
                + "in any order and respecting the keys of the properties:\n\"" + PROP_PREFIX + "=some prefix\", "
                + "\"" + PROP_CONTAINED + "=some contained string\", \"" + PROP_SUFFIX + "=some suffix\".";
        String dirName = getStringAt(listExecResult, 0, postMessage);
        String prefixContainedOrSuffix1 = getStringAt(listExecResult, 1, postMessage, null);
        String prefixContainedOrSuffix2 = getStringAt(listExecResult, 2, postMessage, null);
        String prefixContainedOrSuffix3 = getStringAt(listExecResult, 3, postMessage, null);
        List<String> list = listFiles(dirName, prefixContainedOrSuffix1, prefixContainedOrSuffix2,
                prefixContainedOrSuffix3);
        log(execParams, list);
        return createResult(list);
    }

    public static List<String> listFiles(String dirName) throws EpeAppException {
        return listFiles(dirName, null, null, null);
    }

    public static List<String> listFiles(String dirName, String prefixContainedOrSuffix1,
            String prefixContainedOrSuffix2, String prefixContainedOrSuffix3) throws EpeAppException {
        String[] prefixContainedAndSuffix = retrievePrefixContainedAndSuffix(prefixContainedOrSuffix1,
                prefixContainedOrSuffix2, prefixContainedOrSuffix3);
        return listFiles(dirName, prefixContainedAndSuffix);
    }

    public static boolean isValidFileOrDir(String dirOrFileName, String[] prefixContainedAndSuffix)
            throws EpeAppException {
        EpeAppUtils.checkNull("dirOrFileName", dirOrFileName);
        dirOrFileName = EpeAppUtils.cleanFilename(dirOrFileName);
        dirOrFileName = dirOrFileName.endsWith("/") ? dirOrFileName.substring(0, dirOrFileName.length() - 1)
                : dirOrFileName;
        String prefix = prefixContainedAndSuffix[0];
        String contained = prefixContainedAndSuffix[1];
        String suffix = prefixContainedAndSuffix[2];
        if (!EpeAppUtils.isEmpty(prefix) && !dirOrFileName.startsWith(prefix))
            return false;
        if (!EpeAppUtils.isEmpty(contained) && !dirOrFileName.contains(contained))
            return false;
        if (!EpeAppUtils.isEmpty(suffix) && !dirOrFileName.endsWith(suffix))
            return false;
        return true;
    }

    public static List<String> listFiles(String dirName, String[] prefixContainedAndSuffix) throws EpeAppException {
        File dir = new File(dirName);
        EpeAppUtils.checkDir(dir);
        List<String> list = new ArrayList<>();
        String[] listFileName = dir.list();

        for (String fileName : listFileName) {
            if (isValidFileOrDir(fileName, prefixContainedAndSuffix))
                list.add(fileName);
        }

        Collections.sort(list);
        return list;
    }

    public static String[] retrievePrefixContainedAndSuffix(String str1, String str2, String str3)
            throws EpeAppException {
        String[] prefixContainedAndSuffix = { null, null, null };
        retrievePrefixContainedOrSuffixByStr(str1, prefixContainedAndSuffix);
        retrievePrefixContainedOrSuffixByStr(str2, prefixContainedAndSuffix);
        retrievePrefixContainedOrSuffixByStr(str3, prefixContainedAndSuffix);
        return prefixContainedAndSuffix;
    }

    private static void retrievePrefixContainedOrSuffixByStr(String str, String[] prefixContainedAndSuffix)
            throws EpeAppException {
        if (EpeAppUtils.isEmpty(str))
            return;
        Map.Entry<String, String> filePathAndLast = EpeAppUtils.retrieveKeyAndValue(str);

        if (filePathAndLast.getKey().equals(PROP_PREFIX)) {
            prefixContainedAndSuffix[0] = filePathAndLast.getValue();
        } else if (filePathAndLast.getKey().equals(PROP_CONTAINED)) {
            prefixContainedAndSuffix[1] = filePathAndLast.getValue();
        } else if (filePathAndLast.getKey().equals(PROP_SUFFIX)) {
            prefixContainedAndSuffix[2] = filePathAndLast.getValue();
        } else {
            throw new EpeAppException("Unknown key: " + filePathAndLast.getKey());
        }
    }

}
