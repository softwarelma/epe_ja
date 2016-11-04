package com.softwarelma.epe.p3.mem;

import java.util.HashMap;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;

public final class EpeMem {

	private final Map<String, EpeExecContent> mapVarNameExecContent = new HashMap<>();

	public void putAlsoNull(String varName, EpeExecContent execContent) throws EpeAppException {
		EpeAppUtils.checkNull("varName", varName);
		EpeAppUtils.checkNull("execContent", execContent);

		if (execContent.getContentInternal() == null) {
			this.mapVarNameExecContent.remove(varName);
			return;
		}

		this.mapVarNameExecContent.put(varName, execContent);
	}

	public EpeExecContent getAlsoNull(String varName) throws EpeAppException {
		EpeAppUtils.checkNull("varName", varName);
		EpeExecContent execContent = this.mapVarNameExecContent.get(varName);
		execContent = execContent == null ? new EpeExecContent(null) : execContent;
		return execContent;
	}

}
