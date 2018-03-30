package com.softwarelma.epe.p2.exec;

import java.util.HashSet;
import java.util.Set;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public abstract class EpeExecAbstractFactory implements EpeExecFactoryInterface {

    private static final Set<String> setFuncClassNameValid = new HashSet<>();
    private static final Set<String> setFuncClassNameInvalid = new HashSet<>();

    @Override
    public boolean isFunc(String funcName) throws EpeAppException {
        String className = this.getClassName(funcName);

        if (setFuncClassNameValid.contains(className)) {
            return true;
        } else if (setFuncClassNameInvalid.contains(className)) {
            return false;
        }

        try {
            Class.forName(className);

            synchronized (setFuncClassNameValid) {
                setFuncClassNameValid.add(className);
            }

            return true;
        } catch (ClassNotFoundException e) {
            synchronized (setFuncClassNameInvalid) {
                setFuncClassNameInvalid.add(className);
            }

            return false;
        }
    }

    @Override
    public EpeExecInterface getNewFuncInstance(String funcName) throws EpeAppException {
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

    public String getClassName(String funcName) throws EpeAppException {
        EpeAppUtils.checkNull("funcName", funcName);

        if (!funcName.toLowerCase().equals(funcName)) {
            throw new EpeAppException("Func name \"" + funcName + "\" should be in lower case");
        }

        String className = funcName.substring(0, 1).toUpperCase() + funcName.substring(1);
        className = this.getClassNamePrefix() + className;
        return className;
    }

    @Override
    public String getClassNamePrefix() {
        String[] arrayClassName = this.getClass().getName().split("\\.");
        String className = arrayClassName[arrayClassName.length - 1];// e.g. EpePackGenericFactory
        String packageName = className.substring("EpePack".length());
        packageName = packageName.substring(0, packageName.length() - "Factory".length());// e.g. Generic
        String classNamePrefix = "com.softwarelma.epe.p3." + packageName.toLowerCase() + ".Epe" + packageName + "Final";
        return classNamePrefix;// e.g. com.softwarelma.epe.p3.generic.EpeGenericFinal
    }

}
