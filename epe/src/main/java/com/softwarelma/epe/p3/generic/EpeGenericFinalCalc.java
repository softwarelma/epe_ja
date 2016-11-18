package com.softwarelma.epe.p3.generic;

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

        return obj + "";
    }

}
