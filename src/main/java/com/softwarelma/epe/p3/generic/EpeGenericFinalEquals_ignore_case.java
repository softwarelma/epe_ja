package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalEquals_ignore_case extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "equals_ignore_case, expected string1 and string2, or list1 and list2 or listList1 and listList2.";

        if (isListListStringAt(listExecResult, 0, postMessage)) {
            return doFuncListList(execParams, listExecResult, postMessage);
        } else if (isListStringAt(listExecResult, 0, postMessage)) {
            return doFuncList(execParams, listExecResult, postMessage);
        } else if (isStringAt(listExecResult, 0, postMessage)) {
            return doFuncString(execParams, listExecResult, postMessage);
        }

        throw new EpeAppException("Parameters not valid. " + postMessage);
    }

    public EpeExecResult doFuncListList(EpeExecParams execParams, List<EpeExecResult> listExecResult,
            String postMessage) throws EpeAppException {
        List<List<String>> listList1 = getListListStringAt(listExecResult, 0, postMessage);
        List<List<String>> listList2 = getListListStringAt(listExecResult, 1, postMessage);
        String str = equalsIgnoreCaseByListList(listList1, listList2) + "";
        log(execParams, str);
        return createResult(str);
    }

    public EpeExecResult doFuncList(EpeExecParams execParams, List<EpeExecResult> listExecResult, String postMessage)
            throws EpeAppException {
        List<String> list1 = getListStringAt(listExecResult, 0, postMessage);
        List<String> list2 = getListStringAt(listExecResult, 1, postMessage);
        String str = equalsIgnoreCaseByList(list1, list2) + "";
        log(execParams, str);
        return createResult(str);
    }

    public EpeExecResult doFuncString(EpeExecParams execParams, List<EpeExecResult> listExecResult, String postMessage)
            throws EpeAppException {
        String text1 = getStringAt(listExecResult, 0, postMessage);
        String text2 = getStringAt(listExecResult, 1, postMessage);
        String str = equalsIgnoreCaseByString(text1, text2) + "";
        log(execParams, str);
        return createResult(str);
    }

    public static boolean equalsIgnoreCaseByListList(List<List<String>> listList1, List<List<String>> listList2)
            throws EpeAppException {
        if (listList1 == null && listList2 == null) {
            return true;
        } else if (listList1 == null || listList2 == null) {
            return false;
        } else if (listList1.size() != listList2.size()) {
            return false;
        }

        List<String> list1, list2;

        for (int i = 0; i < listList1.size(); i++) {
            list1 = listList1.get(i);
            list2 = listList2.get(i);

            if (!equalsIgnoreCaseByList(list1, list2)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equalsIgnoreCaseByList(List<String> list1, List<String> list2) throws EpeAppException {
        if (list1 == null && list2 == null) {
            return true;
        } else if (list1 == null || list2 == null) {
            return false;
        } else if (list1.size() != list2.size()) {
            return false;
        }

        String str1, str2;

        for (int i = 0; i < list1.size(); i++) {
            str1 = list1.get(i);
            str2 = list2.get(i);

            if (!equalsIgnoreCaseByString(str1, str2)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equalsIgnoreCaseByString(String str1, String str2) throws EpeAppException {
        if (str1 == null && str2 == null) {
            return true;
        } else if (str1 == null || str2 == null) {
            return false;
        }

        return str1.equalsIgnoreCase(str2);
    }

}
