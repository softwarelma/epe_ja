package com.softwarelma.epe.p2.prog;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;

public final class EpeProgDefaultSent extends EpeProgAbstractSent {

	public EpeProgDefaultSent(String type, String varName, String literal, String funcName, List<String> listParam)
			throws EpeAppException {
		super(type, varName, literal, funcName, listParam);
	}

}
