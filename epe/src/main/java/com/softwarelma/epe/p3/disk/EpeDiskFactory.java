package com.softwarelma.epe.p3.disk;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeDiskFactory {

	private static EpeDiskFactory diskFactory;

	public static EpeDiskFactory getInstance() {
		if (diskFactory != null) {
			return diskFactory;
		}

		synchronized (EpeDiskFactory.class) {
			if (diskFactory != null) {
				return diskFactory;
			}

			EpeDiskFactory diskFactory2 = new EpeDiskFactory();
			diskFactory = diskFactory2;
			return diskFactory;
		}
	}

	private EpeDiskFactory() {
	}

	public boolean isDisk(String funcName) throws EpeAppException {
		String className = this.getClassName(funcName);

		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public EpeDiskInterface getNewInstance(String funcName) throws EpeAppException {
		String className = this.getClassName(funcName);

		try {
			return (EpeDiskInterface) Class.forName(className).newInstance();
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
		return "com.softwarelma.epe.p3.disk.EpeDiskFinal";
	}

}
