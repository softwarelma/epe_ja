package com.softwarelma.epe.p3.xml;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecInterface;

public final class EpeXmlFactory {

    private static EpeXmlFactory funcFactory;

    public static EpeXmlFactory getInstance() {
        if (funcFactory != null) {
            return funcFactory;
        }

        synchronized (EpeXmlFactory.class) {
            if (funcFactory != null) {
                return funcFactory;
            }

            EpeXmlFactory funcFactory2 = new EpeXmlFactory();
            funcFactory = funcFactory2;
            return funcFactory;
        }
    }

    private EpeXmlFactory() {
    }

    public boolean isFunc(String funcName) throws EpeAppException {
        String className = this.getClassName(funcName);

        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public EpeExecInterface getNewInstance(String funcName) throws EpeAppException {
        String className = this.getClassName(funcName);

        try {
            return (EpeExecInterface) Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            throw new EpeAppException("Unknown func \"" + funcName + "\"", e);
        } catch (InstantiationException e) {
            throw new EpeAppException("Unknown func \"" + funcName + "\"", e);
        } catch (IllegalAccessException e) {
            throw new EpeAppException("Unknown func \"" + funcName + "\"", e);
        }
    }

    private String getClassName(String funcName) throws EpeAppException {
        EpeAppUtils.checkNull("funcName", funcName);

        if (!funcName.toLowerCase().equals(funcName)) {
            throw new EpeAppException("Func name \"" + funcName + "\" should be in lower case");
        }

        String className = funcName.substring(0, 1).toUpperCase() + funcName.substring(1);
        className = this.getClassNamPrefix() + className;
        return className;
    }

    private String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.xml.EpeXmlFinal";
    }

}
