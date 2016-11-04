package com.softwarelma.epe.p2.prog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeProgParser {

	public List<EpeProgSentInterface> retrieveProgSentList(String programContent) throws EpeAppException {
		EpeAppUtils.checkNull("programContent", programContent);
		List<EpeProgSentInterface> listProgSent = new ArrayList<>();
		String endOfProgram = "\n";
		programContent = programContent.endsWith(endOfProgram) ? programContent : programContent + endOfProgram;

		Map<String, String> mapNotContainedReplaced = new HashMap<>();
		String ncs = EpeAppUtils.getNotContainedString(programContent, "[", "]");
		String sDoubleBackSlash = EpeAppUtils.getNotContainedString(ncs, 0, "]");
		String sBackSlashedQuote = EpeAppUtils.getNotContainedString(ncs, 1, "]");
		programContent = cleanProgramContent(programContent, mapNotContainedReplaced, sDoubleBackSlash,
				sBackSlashedQuote);
		String[] sSplit = programContent.split(";");

		for (String token : sSplit) {
			String sentStr = token.trim();

			if (sentStr.isEmpty()) {
				continue;
			}

			EpeProgSentInterface progSent = getProgSent(sentStr, mapNotContainedReplaced, sDoubleBackSlash,
					sBackSlashedQuote);
			// System.out.println("\t{" + sentStr + "} - isSent " + isSent);
			sentStr = replaceNotContainedWithReplaced(sentStr, mapNotContainedReplaced);
			// String s = " ";
			// System.out.println(sentStr + s.substring(sentStr.length()) +
			// progSent.getType() + "\n");
			listProgSent.add(progSent);
		}

		if (listProgSent.isEmpty()) {
			throw new EpeAppException("Void program");
		}

		return listProgSent;
	}

	private EpeProgSentInterface getProgSent(String sentStr, Map<String, String> mapNotContainedReplaced,
			String sDoubleBackSlash, String sBackSlashedQuote) throws EpeAppException {
		sentStr = sentStr.replace(" ", "");
		sentStr = sentStr.replace("\t", "");

		// String reRight = "(" + "(" + reFunc + ")|(" + reNull + ")|(" + reStr
		// + ")|(" + reId + ")" + ")";
		int[] posString;
		String sentType;
		EpeProgSentInterface progSent;
		String[] textSplit;

		posString = indexOf(sentStr, EpeAppConstants.REGEX_NULL);
		sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "null_type" : null;

		if (sentType != null) {
			progSent = new EpeProgDefaultSent(sentType, null, sentStr, null, null);
			// System.out.println(" " + progSent);
			return progSent;
		}

		posString = indexOf(sentStr, EpeAppConstants.REGEX_STR_INTERNAL);
		sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "str_type" : null;

		if (sentType != null) {
			String literal = mapNotContainedReplaced.get(mapNotContainedReplaced.get(sentStr));
			literal = literal.replace(sBackSlashedQuote, "\\\"");
			literal = literal.replace(sDoubleBackSlash, "\\\\");
			progSent = new EpeProgDefaultSent(sentType, null, literal, null, null);
			// System.out.println(" " + progSent);
			return progSent;
		}

		posString = indexOf(sentStr, EpeAppConstants.REGEX_FUNC);
		sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "func_type" : null;

		if (sentType != null) {
			progSent = getProgSentFromFuncSent(sentStr);
			// System.out.println(" " + progSent);
			return progSent;
		}

		posString = indexOf(sentStr, EpeAppConstants.REGEX_ID);
		sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "id_type" : null;

		if (sentType != null) {
			progSent = new EpeProgDefaultSent(sentType, null, sentStr, null, null);
			// System.out.println(" " + progSent);
			return progSent;
		}

		posString = indexOf(sentStr, EpeAppConstants.REGEX_LEFT_NULL);
		sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "left_null_type" : null;

		if (sentType != null) {
			textSplit = sentStr.split("=");
			progSent = new EpeProgDefaultSent(sentType, textSplit[0], textSplit[1], null, null);
			// System.out.println(" " + progSent);
			return progSent;
		}

		posString = indexOf(sentStr, EpeAppConstants.REGEX_LEFT_STR);
		sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "left_str_type" : null;

		if (sentType != null) {
			textSplit = sentStr.split("=");
			String literal = mapNotContainedReplaced.get(textSplit[1]);
			literal = literal.replace(sBackSlashedQuote, "\\\"");
			literal = literal.replace(sDoubleBackSlash, "\\\\");
			progSent = new EpeProgDefaultSent(sentType, textSplit[0], literal, null, null);
			// System.out.println(" " + progSent);
			return progSent;
		}

		posString = indexOf(sentStr, EpeAppConstants.REGEX_LEFT_FUNC);
		sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "left_func_type" : null;

		if (sentType != null) {
			progSent = getProgSentFromFuncSent(sentStr);
			// System.out.println(" " + progSent);
			return progSent;
		}

		posString = indexOf(sentStr, EpeAppConstants.REGEX_LEFT_ID);
		sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "left_id_type" : null;

		if (sentType != null) {
			textSplit = sentStr.split("=");
			progSent = new EpeProgDefaultSent(sentType, textSplit[0], textSplit[1], null, null);
			// System.out.println(" " + progSent);
			return progSent;
		}

		sentStr = replaceNotContainedWithReplaced(sentStr, mapNotContainedReplaced);
		sentStr = sentStr.replace(sBackSlashedQuote, "\\\"");
		sentStr = sentStr.replace(sDoubleBackSlash, "\\\\");
		throw new EpeAppException("Unrecognized sentence: " + sentStr);
	}

	private EpeProgSentInterface getProgSentFromFuncSent(String funcSent) throws EpeAppException {
		String sentType = "func_type";
		String varName = null;
		String[] textSplit;

		if (funcSent.contains("=")) {
			sentType = "left_func_type";
			textSplit = funcSent.split("=");
			varName = textSplit[0];
			funcSent = textSplit[1];
		}

		textSplit = funcSent.split("\\(");
		String funcName = textSplit[0];
		String params = textSplit[1].substring(0, textSplit[1].length() - 1);
		textSplit = params.split("\\,");
		List<String> listParam = Arrays.asList(textSplit);
		EpeProgSentInterface progSent = new EpeProgDefaultSent(sentType, varName, null, funcName, listParam);
		return progSent;
	}

	private String replaceNotContainedWithReplaced(String sentStr, Map<String, String> mapNotContainedReplaced) {
		for (String notContained : mapNotContainedReplaced.keySet()) {
			sentStr = sentStr.replace(notContained, mapNotContainedReplaced.get(notContained));
		}

		return sentStr;
	}

	private String cleanProgramContent(String programContent, Map<String, String> mapNotContainedReplaced,
			String sDoubleBackSlash, String sBackSlashedQuote) throws EpeAppException {
		programContent = programContent.replace("\r\n", "\n");
		programContent = programContent.replace("\r", "");
		programContent = cleanCommentOrString(programContent, true, mapNotContainedReplaced);
		programContent = programContent.replace("\\\\", sDoubleBackSlash);
		programContent = programContent.replace("\\\"", sBackSlashedQuote);
		programContent = cleanCommentOrString(programContent, false, mapNotContainedReplaced);
		cleanProgramContentValidation(programContent);
		return programContent;
	}

	private void cleanProgramContentValidation(String programContent) throws EpeAppException {
		if (programContent.contains("\\")) {
			String[] sSplit = programContent.split(";");

			for (String sentStr : sSplit) {
				if (programContent.contains("\\")) {
					throw new EpeAppException("Unrecognized sentence: " + sentStr);
				}
			}
		}
	}

	private String cleanCommentOrString(String text, boolean iscomment, Map<String, String> mapNotContainedReplaced)
			throws EpeAppException {
		// System.out.println(text);
		// System.out.println("---------------------");
		String text2 = text;
		int iter = 0;
		String notContained = null;

		if (!iscomment) {
			notContained = EpeAppUtils.getNotContainedString(text);
		}

		do {
			text = text2;
			if (iscomment) {
				text2 = cleanCommentOnce(text);
			} else {
				text2 = cleanStringOnce(text, notContained, iter++, mapNotContainedReplaced);
			}

			// System.out.println(text2);
			// System.out.println("---------------------");
		} while (text2 != null);

		return text;
	}

	private String cleanStringOnce(String text, String notContained, int iter,
			Map<String, String> mapNotContainedReplaced) throws EpeAppException {
		// System.out.println(text);
		// System.out.println("---------------------");

		int[] posString = indexOf(text, EpeAppConstants.REGEX_STR);
		// int[] posString = indexOf(text, "\"(((?!\").)*\\n*)*\"", 0, 3);

		// System.out.println("posBlockComment " + posBlockComment[0] + ", " +
		// posBlockComment[1]);
		// System.out.println("---------------------");

		if (posString[0] == -1) {
			return null;
		}

		notContained = EpeAppUtils.getNotContainedString(notContained, iter);
		String replaced = text.substring(posString[0], posString[1]);
		mapNotContainedReplaced.put(notContained, replaced);
		text = text.replace(replaced, notContained);
		// text = text.substring(0, posString[0]) + notContained +
		// text.substring(posString[1], text.length());
		// System.out.println(text);
		// System.out.println("---------------------");
		return text;
	}

	private String cleanCommentOnce(String text) throws EpeAppException {
		// System.out.println(text);
		// System.out.println("---------------------");

		int[] posLineComment = indexOf(text, EpeAppConstants.REGEX_COMMENT_LINE);
		posLineComment[1] = posLineComment[0] == -1 ? -1 : posLineComment[1] - 1;
		// System.out.println("posLineComment " + posLineComment[0] + ", " +
		// posLineComment[1]);
		int[] posBlockComment = indexOf(text, EpeAppConstants.REGEX_COMMENT_BLOCK);
		// System.out.println("posBlockComment " + posBlockComment[0] + ", " +
		// posBlockComment[1]);
		// System.out.println("---------------------");

		int[] posComment;
		if (posLineComment[0] == -1 && posBlockComment[0] == -1) {
			posComment = posLineComment;
		} else if (posLineComment[0] != -1 && posBlockComment[0] != -1) {
			posComment = posLineComment[0] < posBlockComment[0] ? posLineComment : posBlockComment;
		} else {
			posComment = posLineComment[0] > posBlockComment[0] ? posLineComment : posBlockComment;
		}
		// System.out.println("posComment " + posComment[0] + ", " +
		// posComment[1]);
		// System.out.println("---------------------");

		if (posComment[0] == -1) {
			return null;
		}
		text = text.substring(0, posComment[0]) + text.substring(posComment[1], text.length());
		// System.out.println(text);
		// System.out.println("---------------------");
		return text;
	}

	private int[] indexOf(String text, String patternStr) {
		boolean big = patternStr.equals(EpeAppConstants.REGEX_STR_INTERNAL)
				|| patternStr.equals(EpeAppConstants.REGEX_LEFT_STR) || patternStr.equals(EpeAppConstants.REGEX_STR)
				|| patternStr.equals(EpeAppConstants.REGEX_COMMENT_BLOCK);

		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(text);
		int[] ret = { -1, -1 };

		if (matcher.find()) {
			ret = new int[] { matcher.start(), matcher.end() };
		}

		if (big && ret[0] != -1) {
			String text2 = text.substring(0, ret[1] - 1);
			int[] ret2 = indexOf(text2, patternStr);
			ret = ret2[0] == -1 ? ret : ret2;
		}

		return ret;
	}

}
