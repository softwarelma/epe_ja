package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalJoin extends EpeGenericAbstract {

	@Override
	public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
		String postMessage = "join, expected the list and optionally the separator str.";
		List<String> listStr = getListStringAt(listExecResult, 0, postMessage);
		String sep = getStringAt(listExecResult, 1, postMessage);
		String str = join(listStr, sep);
		log(execParams, str);
		return createResult(str);
	}

	public static String join(List<String> listStr, String sep) {
		StringBuilder sb = new StringBuilder();
		String sep2 = "";
		for (String str : listStr) {
			sb.append(sep2);
			sep2 = sep;
			sb.append(str);
		}
		return sb.toString();
	}

}
