package com.softwarelma.epe.p2.exec;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;

public final class EpeExecDefaultSent extends EpeExecAbstractSent {

	public EpeExecDefaultSent(String varName, String literal, String funcName, List<String> listParam)
			throws EpeAppException {
		super(varName, literal, funcName, listParam);
	}

}
