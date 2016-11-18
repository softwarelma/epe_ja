package com.softwarelma.epe.p3.generic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalList_funcs extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "list_funcs, expected optionally the package (db, disk, echo, generic, print, xml, etc.";
        String pack = this.getStringAt(listExecResult, 0, postMessage, null);
        List<String> listFunc = retriveListFunc(pack);
        this.log(execParams, listFunc);
        return this.createResult(listFunc);
    }

    public static List<String> retriveListFunc(String pack) throws EpeAppException {
        List<String> listFunc = new ArrayList<>();
        String packPrefix = "com/softwarelma/epe/p3/";
        String classPrefixStart = "com.softwarelma.epe.p3.";
        String classPrefixEnd = "final";

        if (pack == null) {
            retriveListFunc(packPrefix + "db", classPrefixStart + "db.epedb" + classPrefixEnd, listFunc);
            retriveListFunc(packPrefix + "disk", classPrefixStart + "disk.epedisk" + classPrefixEnd, listFunc);
            retriveListFunc(packPrefix + "echo", classPrefixStart + "echo.epeecho" + classPrefixEnd, listFunc);
            retriveListFunc(packPrefix + "generic", classPrefixStart + "generic.epegeneric" + classPrefixEnd, listFunc);
            retriveListFunc(packPrefix + "print", classPrefixStart + "print.epeprint" + classPrefixEnd, listFunc);
            retriveListFunc(packPrefix + "xml", classPrefixStart + "xml.epexml" + classPrefixEnd, listFunc);
        } else {
            retriveListFunc(packPrefix + pack, classPrefixStart + pack + ".epe" + pack + classPrefixEnd, listFunc);
        }

        return listFunc;
    }

    public static void retriveListFunc(String slashedPack, String classPrefixLowerCase, List<String> listFunc)
            throws EpeAppException {
        List<Class<?>> classes = retrieveClasses(EpeGenericFinalCalc.class.getClassLoader(), slashedPack);

        for (Class<?> c : classes) {
            String cStr = c.getName();

            if (!cStr.contains("$") && cStr.toLowerCase().startsWith(classPrefixLowerCase)) {
                listFunc.add(cStr.substring(classPrefixLowerCase.length()).toLowerCase());
            }
        }
    }

    // FIXME test from jar
    // from http://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
    public static List<Class<?>> retrieveClasses(ClassLoader cl, String slashedPack) throws EpeAppException {
        try {
            String dottedPackage = slashedPack.replaceAll("[/]", ".");
            List<Class<?>> classes = new ArrayList<>();
            URL upackage = cl.getResource(slashedPack);
            // DataInputStream dis = new DataInputStream((InputStream) upackage.getContent());
            BufferedReader dis = new BufferedReader(new InputStreamReader((InputStream) upackage.getContent()));
            String line = null;

            while ((line = dis.readLine()) != null) {
                if (line.endsWith(".class")) {
                    classes.add(Class.forName(dottedPackage + "." + line.substring(0, line.lastIndexOf('.'))));
                }
            }

            return classes;
        } catch (Exception e) {
            throw new EpeAppException("retrieveClasses from slashedPack: " + slashedPack, e);
        }
    }

}
