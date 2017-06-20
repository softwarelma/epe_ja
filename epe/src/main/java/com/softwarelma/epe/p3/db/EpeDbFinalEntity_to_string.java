package com.softwarelma.epe.p3.db;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDbFinalEntity_to_string extends EpeDbAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        // TODO
        return null;
    }

    public static String toString(String columnName, Object obj, String avoidingClasses) throws EpeAppException {
        EpeAppUtils.checkEmpty("columnName", columnName);
        avoidingClasses = avoidingClasses == null ? "" : "," + avoidingClasses.trim() + ",";

        if (obj == null) {
            return "";
        }

        Class<?> clazz = obj.getClass();
        String str;

        if (obj instanceof String) {
            str = obj.toString();
        } else if (obj instanceof BigDecimal) {
            str = ((BigDecimal) obj).toPlainString();
        } else if (obj instanceof Integer) {
            str = ((Integer) obj).toString();
        } else if (obj instanceof Timestamp) {
            Timestamp ts = (Timestamp) obj;
            str = new SimpleDateFormat("yyyyMMddHHmmss").format(ts);
            str = str.endsWith("000000") ? str.substring(0, 8) : str;
            System.out.println("timestamp=" + str);
        } else if (obj instanceof Clob) {
            if (avoidingClasses.contains("," + Clob.class.getName() + ",")) {
                return "[CLOB]";
            }

            try {
                Clob clob = (Clob) obj;
                str = clob.getSubString(1, (int) clob.length());
            } catch (SQLException e) {
                throw new EpeAppException("To string for column " + columnName, e);
            }
        } else {
            // str = obj.toString();
            String className = clazz.getName();
            throw new EpeAppException("Unknown class " + className + " for column " + columnName);
        }

        return str;
    }

}
