package com.softwarelma.epe.p3.func;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeFuncFactory {

	private static EpeFuncFactory funcFactory;

	public static EpeFuncFactory getInstance() {
		if (funcFactory != null) {
			return funcFactory;
		}

		synchronized (EpeFuncFactory.class) {
			if (funcFactory != null) {
				return funcFactory;
			}

			EpeFuncFactory funcFactory2 = new EpeFuncFactory();
			funcFactory = funcFactory2;
			return funcFactory;
		}
	}

	private EpeFuncFactory() {
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

	public EpeFuncInterface getNewInstance(String funcName) throws EpeAppException {
		String className = this.getClassName(funcName);

		try {
			return (EpeFuncInterface) Class.forName(className).newInstance();
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
		return "com.softwarelma.epe.p3.func.EpeFuncFinal";
	}

}
