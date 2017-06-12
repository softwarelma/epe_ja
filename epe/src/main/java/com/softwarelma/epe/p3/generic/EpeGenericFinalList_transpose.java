package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.disk.EpeDiskAbstract;

/**
 * @author ellison
 *
 */
public final class EpeGenericFinalList_transpose extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_transpose, expected the list of lists.";
        List<List<String>> listListStr = this.getListListStringAt(listExecResult, 0, postMessage);
        listListStr = transpose(listListStr);
        this.log(execParams, listListStr, null);
        return this.createResult(listListStr, null);
    }

    public static List<List<String>> transpose(List<List<String>> listListStr) throws EpeAppException {
        EpeAppUtils.checkEmptyList("listListStr", listListStr);
        EpeAppUtils.checkEmptyList("listListStr.get(0)", listListStr.get(0));
        List<List<String>> listListStrTransposed = new ArrayList<>();
        List<String> listStrTransposed;
        List<String> listStr;
        int sizeInternal = listListStr.get(0).size();

        for (int j = 0; j < sizeInternal; j++) {
            listStrTransposed = new ArrayList<>();

            for (int i = 0; i < listListStr.size(); i++) {
                listStr = listListStr.get(i);
                EpeAppUtils.checkEmptyList("listListStr.get(" + i + ")", listStr);
                EpeAppUtils.checkRange(listStr.size(), sizeInternal, sizeInternal, false, false,
                        "A matrix must be rectangular to be transposed.");
                listStrTransposed.add(listStr.get(j));
            }

            listListStrTransposed.add(listStrTransposed);
        }

        return listListStrTransposed;
    }

}
