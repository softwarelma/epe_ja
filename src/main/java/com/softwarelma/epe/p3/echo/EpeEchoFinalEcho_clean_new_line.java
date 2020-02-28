package com.softwarelma.epe.p3.echo;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.generic.EpeGenericFinalTrim_full;

public final class EpeEchoFinalEcho_clean_new_line extends EpeEchoAbstract {

	public static final String CUTTING_LENGTH = "cutting_length";

	@Override
	public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
		String postMessage = "echo_clean_new_line, expected the text and eventually the cutting_length prop.";
		String cuttingLengthStr = retrievePropValueOrDefault("echo_clean_new_line", listExecResult, CUTTING_LENGTH, "75");
		int cuttingLength = Integer.parseInt(cuttingLengthStr);
		String str = doEcho_clean_new_line(execParams, listExecResult, cuttingLength, postMessage);
		log(execParams, str);
		return createResult(str);
	}

	public static String doEcho_clean_new_line(EpeExecParams execParams, List<EpeExecResult> listExecResult, int cuttingLength, String postMessage)
			throws EpeAppException {
		EpeAppUtils.checkNull("execParams", execParams);
		EpeAppUtils.checkNull("listExecResult", listExecResult);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < listExecResult.size(); i++) {
			EpeExecResult result = listExecResult.get(i);
			if (isPropAt(listExecResult, i, postMessage))
				continue;
			EpeExecContent content = result.getExecContent();
			if (content.getContentInternal() == null) {
				sb.append("");
			} else {
				String str = content.getContentInternal().toString();
				str = doEcho_clean_new_line(str, cuttingLength);
				sb.append(str);
			}
		}
		return sb.toString();
	}

	public static String doEcho_clean_new_line(String str, int cuttingLength) throws EpeAppException {
		StringBuilder sb = new StringBuilder();
		String[] arrayLine = str.split("\n");
		String sepNewLine = "";
		String sepBackspace = "";
		for (int i = 0; i < arrayLine.length; i++) {
			String prev = i == 0 ? "" : arrayLine[i - 1];
			String line = arrayLine[i];
			if (isNewLine(prev, line, cuttingLength)) {
				sb.append(sepNewLine);
			} else {
				sb.append(sepBackspace);
			}
			sepNewLine = "\n";
			sepBackspace = " ";
			sb.append(line);
		}
		return sb.toString();
	}

	public static boolean isNewLine(String prev, String line, int cuttingLength) throws EpeAppException {
		prev = EpeGenericFinalTrim_full.retrieveTrimFull(prev);
		if (endsWithDot(prev))
			return true;
		if (isShort(prev, cuttingLength))
			return true;
		if (prev.equals(prev.toUpperCase()))
			return true;
		if (line.equals(line.toUpperCase()))
			return true;
		String lineTrim = EpeGenericFinalTrim_full.retrieveTrimFull(line);
		if (startsWithNumber(lineTrim))
			return true;
		if (startsWithHyphen(lineTrim))
			return true;
		if (isShort(line, cuttingLength) && !endsWithDot(line) && !lineTrim.endsWith(":"))
			return true;
		return false;
	}

	public static boolean startsWithNumber(String line) {
		return line.length() > 0 && "0123456789".contains(line.substring(0, 1));
	}

	public static boolean startsWithHyphen(String line) {
		return line.startsWith("\u2014") || line.startsWith("-");
	}

	public static boolean isShort(String line, int cuttingLength) {
		return line.length() < cuttingLength;
	}

	public static boolean endsWithDot(String line) {
		return line.endsWith(".") || line.endsWith("?") || line.endsWith("!");
	}

}
