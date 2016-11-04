package com.softwarelma.epe.p3.db;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;

public final class EpeDbFake extends EpeDbAbstract {

	@Override
	public EpeExecContent doFunc(EpeExecParams execParams, List<EpeExecContent> listFuncContent)
			throws EpeAppException {
		throw new EpeAppException("Not implemented");
	}

}
