package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalList_packs extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        List<String> listPack = retriveListPack();
        log(execParams, listPack);
        return createResult(listPack);
    }

    public static List<String> retriveListPack() throws EpeAppException {
        List<String> listFunc = new ArrayList<>();
        String packP2 = "com.softwarelma.epe.p2.pack";
        List<Class<?>> listClass = null;
        String classStr;
        String classPrefixLowerCase = packP2 + ".epepack";

        try {
            listClass = EpeGenericFinalList_funcs.getClassesForPackage(packP2);
        } catch (ClassNotFoundException e) {
            throw new EpeAppException("retriveListPack ClassNotFoundException", e);
        } catch (Exception e) {
            throw new EpeAppException("retriveListFunc " + e.getClass().getName(), e);
        }

        for (Class<?> clazz : listClass) {
            classStr = clazz.getName();

            if (!classStr.contains("$")) {
                listFunc.add(classStr.substring(classPrefixLowerCase.length(), classStr.length() - 7).toLowerCase());
            }
        }

        return listFunc;
    }

}
