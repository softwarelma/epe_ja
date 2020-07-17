package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalFind_between extends EpeGenericAbstract {

	@Override
	public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
		String postMessage = "find_between, expected the text, the prefix, the suffix and optionally the default.";
		String prefix = getStringAt(listExecResult, 1, postMessage);
		String suffix = getStringAt(listExecResult, 2, postMessage);
		String defaultStr = getStringAt(listExecResult, 3, postMessage, null);
		if (isStringAt(listExecResult, 0, postMessage)) {
			String str = getStringAt(listExecResult, 0, postMessage);
			str = findBetween(str, prefix, suffix, defaultStr);
			log(execParams, str);
			return createResult(str);
		} else if (isListStringAt(listExecResult, 0, postMessage)) {
			List<String> listStr = getListStringAt(listExecResult, 0, postMessage);
			listStr = findBetweenFromList(listStr, prefix, suffix, defaultStr);
			log(execParams, listStr);
			return createResult(listStr);
		} else {
			throw new EpeAppException(postMessage);
		}
	}

	public static List<String> findBetweenFromList(List<String> listStr, String prefix, String suffix) throws EpeAppException {
		return findBetweenFromList(listStr, prefix, suffix, null);
	}

	public static List<String> findBetweenFromList(List<String> listStr, String prefix, String suffix, String defaultStr) throws EpeAppException {
		List<String> listStrRet = new ArrayList<>();
		for (String str : listStr)
			listStrRet.add(findBetween(str, prefix, suffix, defaultStr));
		return listStrRet;
	}

	public static String findBetween(String text, String prefix, String suffix) throws EpeAppException {
		return findBetween(text, prefix, suffix, null);
	}

	public static String findBetween(String text, String prefix, String suffix, String defaultStr) throws EpeAppException {
		EpeAppUtils.checkNull("text", text);
		EpeAppUtils.checkNull("prefix", prefix);
		EpeAppUtils.checkNull("suffix", suffix);
		int indPrefix = text.indexOf(prefix);
		int indSuffix = -1;
		if (indPrefix != -1) {
			String text2 = text.substring(indPrefix + prefix.length());
			indSuffix = text2.indexOf(suffix);
			indSuffix = indSuffix == -1 ? -1 : indPrefix + prefix.length() + indSuffix;
		}
		if (indPrefix != -1 && indSuffix != -1 && indPrefix <= indSuffix)
			return text.substring(indPrefix + prefix.length(), indSuffix);
		return defaultStr;
	}

}
