package com.softwarelma.epe.p3.generic;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalTrim_full extends EpeGenericAbstract {

	@Override
	public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
		String postMessage = "trim, expected text.";
		String text = getStringAt(listExecResult, 0, postMessage);
		String str = retrieveTrimFull(text);
		log(execParams, str);
		return createResult(str);
	}

	public static String retrieveTrimFull(String text) throws EpeAppException {
		EpeAppUtils.checkNull("text", text);
		int ind = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (isUnvisible(c)) {
				ind = i + 1;
			} else {
				break;
			}
		}
		text = text.substring(ind);
		ind = text.length();
		for (int i = text.length() - 1; i >= 0; i--) {
			char c = text.charAt(i);
			if (isUnvisible(c)) {
				ind = i;
			} else {
				break;
			}
		}
		text = text.substring(0, ind);
		return text;
	}

	public static boolean isUnvisible(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

}
