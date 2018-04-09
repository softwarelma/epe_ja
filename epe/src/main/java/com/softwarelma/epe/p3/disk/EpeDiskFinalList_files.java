package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.ArrayList;
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
        File dir = new File(dirName);
        EpeAppUtils.checkDir(dir);

        String str1 = getStringAt(listExecResult, 1, postMessage, null);
        String str2 = getStringAt(listExecResult, 2, postMessage, null);
        String str3 = getStringAt(listExecResult, 3, postMessage, null);
        String[] prefixContainedAndSuffix = retrievePrefixContainedAndSuffix(str1, str2, str3);
        String prefix = prefixContainedAndSuffix[0];
        String contained = prefixContainedAndSuffix[1];
        String suffix = prefixContainedAndSuffix[2];

        List<String> list = new ArrayList<>();
        String[] listFileName = dir.list();

        for (String fileName : listFileName) {
            if (!EpeAppUtils.isEmpty(prefix) && !fileName.startsWith(prefix)) {
                continue;
            }

            if (!EpeAppUtils.isEmpty(contained) && !fileName.contains(contained)) {
                continue;
            }

            if (!EpeAppUtils.isEmpty(suffix) && !fileName.endsWith(suffix)) {
                continue;
            }

            list.add(fileName);
        }

        log(execParams, list);
        return createResult(list);
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
        if (EpeAppUtils.isEmpty(str)) {
            return;
        }

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
