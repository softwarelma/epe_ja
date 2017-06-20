package com.softwarelma.epe.p3.db;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDbFinalEntity_from_string extends EpeDbAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "entity_from_string, expected the table name and the string with the content of the entity.";
        String table = this.getStringAt(listExecResult, 0, postMessage);
        String entityStr = this.getStringAt(listExecResult, 1, postMessage);
        EpeDbEntity entity = newEntityFromString(table, entityStr);
        List<String> listStr = new ArrayList<>();
        // listStr.add("prop=" + value); //for future usage
        this.log(execParams, listStr);
//        return this.createResult(listStr, entity);
        return null;
    }

    public static EpeDbEntity newEntityFromString(String table, String entityStr) throws EpeAppException {
        EpeAppUtils.checkEmpty("table", table);
        EpeAppUtils.checkEmpty("entityStr", entityStr);
        return newInstance(table, entityStr);
    }

    /**
     * Without LF
     * 
     * {-1}: null - {-2}: no LF - {0}: LF is {0}
     * 
     * {1}attr1{-1}{1}attr2{-2}abc{1}attr3{0}ab{0}c
     */
    private static EpeDbEntity newInstance(String table, String source) throws EpeAppException {
        String[] nameCodeValue = retrieveNameCodeValue(source);
        // String name = nameCodeValue[0];
        String code = nameCodeValue[1];
        String valueStr = nameCodeValue[2];
        valueStr = valueStr.replace(code, "\n");
        return newInstanceWithLF(table, valueStr);
    }

    private static String[] retrieveNameCodeValue(String source) throws EpeAppException {
        EpeAppUtils.checkNull("source", source);
        int ind1 = source.indexOf("{");
        String name = source.substring(0, ind1);
        int ind2 = source.indexOf("}") + 1;
        String code = source.substring(ind1, ind2);
        String valueStr = source.substring(ind2);
        return new String[] { name, code, valueStr };
    }

    /**
     * {-1}: null - {-2}: no LF - {0}: LF is {0}
     * 
     * attr1{-1}
     * 
     * attr2{-2}abc
     * 
     * attr3{0}ab{0}c
     */
    private static EpeDbEntity newInstanceWithLF(String table, String source) throws EpeAppException {
        String[] arrayAttribute = source.split("\n");
        // TODO use table
        EpeDbEntity entity = null;// TODO

        for (String attribute : arrayAttribute) {
            String[] nameCodeValue = retrieveNameCodeValue(attribute);
            String name = nameCodeValue[0];
            String code = nameCodeValue[1];
            String valueStrAtt = nameCodeValue[2];
            Object valueObj;
            String classNameAtt = null;// TODO

            if (code.equals("{-1}")) {
                valueObj = null;
            } else if (code.equals("{-2}")) {
                valueObj = newInstanceAttribute(classNameAtt, valueStrAtt);
            } else if (code.contains("-")) {
                throw new EpeAppException("Unknown code: " + code);
            } else {
                valueStrAtt = valueStrAtt.replace(code, "\n");
                valueObj = newInstanceAttribute(classNameAtt, valueStrAtt);
            }

            entity.set(attribute, valueObj);
        }

        return entity;
    }

    private static Object newInstanceAttribute(String className, String valueStr) throws EpeAppException {
        EpeAppUtils.checkEmpty("className", className);
        EpeAppUtils.checkNull("valueStr", valueStr);
        Object valueObj;

        if (className.equals(String.class.getName())) {
            valueObj = valueStr;
        } else if (className.equals(Long.class.getName())) {
            valueObj = EpeAppUtils.parseLong(valueStr);
        } else {
            throw new EpeAppException("Unknown clazz: " + className);
        }

        return valueObj;
    }

}
