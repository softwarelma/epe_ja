package com.softwarelma.epe.p3.generic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalList_packs extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        List<String> listPack = retriveListPack();
        this.log(execParams, listPack);
        return this.createResult(listPack);
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
