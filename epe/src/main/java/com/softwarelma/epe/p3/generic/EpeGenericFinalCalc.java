package com.softwarelma.epe.p3.generic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalCalc extends EpeGenericAbstract {

    /**
     * see also http://docs.oracle.com/javase/7/docs/technotes/guides/scripting/programmer_guide/
     */
    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "calc, expected the text.";
        String text = this.getStringAt(listExecResult, 0, postMessage);
        String str = retrieveCalc(text);
        this.log(execParams, str);
        return this.createResult(str);
    }

    public static String retrieveCalc(String text) throws EpeAppException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        Object obj;

        try {
            obj = engine.eval(text);
        } catch (ScriptException e) {
            throw new EpeAppException("retrieveCalc with text: " + text, e);
        }

        try {
            List<Class> classes;
            classes = getClasses(EpeGenericFinalCalc.class.getClassLoader(), "com/softwarelma/epe/p3/generic");
            for (Class c : classes) {
                System.out.println("Class: " + c);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return obj + "";
    }

    // FIXME test from jar
    // from http://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
    // FIXME should remove classes with $
    public static List<Class> getClasses(ClassLoader cl, String slashedPack) throws Exception {

        String dottedPackage = slashedPack.replaceAll("[/]", ".");
        List<Class> classes = new ArrayList<Class>();
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
    }

}
