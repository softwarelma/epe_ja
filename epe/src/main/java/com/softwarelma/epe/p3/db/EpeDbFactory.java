package com.softwarelma.epe.p3.db;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeDbFactory {

	private static EpeDbFactory dbFactory;

	public static EpeDbFactory getInstance() {
		if (dbFactory != null) {
			return dbFactory;
		}

		synchronized (EpeDbFactory.class) {
			if (dbFactory != null) {
				return dbFactory;
			}

			EpeDbFactory dbFactory2 = new EpeDbFactory();
			dbFactory = dbFactory2;
			return dbFactory;
		}
	}

	private EpeDbFactory() {
	}

	public boolean isDb(String funcName) throws EpeAppException {
		EpeAppUtils.checkNull("funcName", funcName);
		return false;
	}

	public EpeDbInterface getNewInstance(String funcName) throws EpeAppException {
		EpeAppUtils.checkNull("funcName", funcName);
		throw new EpeAppException("Not implemented");
	}

}
