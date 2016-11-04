package com.softwarelma.epe.p1.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public abstract class EpeAppUtils {

	public static void checkNull(String name, String value) throws EpeAppException {
		if (name == null) {
			throw new EpeAppException("Null name");
		}

		if (value == null) {
			throw new EpeAppException("Null " + name);
		}
	}

	public static void checkNull(String name, Object value) throws EpeAppException {
		if (name == null) {
			throw new EpeAppException("Null name");
		}

		if (value == null) {
			throw new EpeAppException("Null " + name);
		}
	}

	public static void checkContains(List<String> list, String name, String value) throws EpeAppException {
		checkNull("name", name);
		checkNull("list", list);

		if (!list.contains(value)) {
			throw new EpeAppException(name + " \"" + value + "\" is not contained in " + list);
		}
	}

	public static void checkRange(int i, int i0, int i1, boolean open0, boolean open1) throws EpeAppException {
		if (open0) {
			if (i < i0) {
				throw new EpeAppException("value " + i + " < " + i0);
			} else if (i == i0) {
				throw new EpeAppException("value " + i + " == " + i0);
			}
		} else {
			if (i < i0) {
				throw new EpeAppException("value " + i + " < " + i0);
			}
		}

		if (open1) {
			if (i > i1) {
				throw new EpeAppException("value " + i + " > " + i1);
			} else if (i == i1) {
				throw new EpeAppException("value " + i + " == " + i1);
			}
		} else {
			if (i > i1) {
				throw new EpeAppException("value " + i + " > " + i1);
			}
		}
	}

	public static void writeFile(String content, String filePath, String encoding, boolean append) throws Exception {
		encoding = encoding == null ? EpeAppConstants.ENCODING_UTF_8 : encoding;
		Charset charset = Charset.forName(encoding);
		Path path = Paths.get(filePath);
		OpenOption optionAppend = append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING;

		try (BufferedWriter writer = Files.newBufferedWriter(path, charset, optionAppend)) {
			writer.write(content, 0, content.length());
		} catch (Exception x) {
			throw x;
		}
	}

	public static String readFileAsStringGuessingEncoding(String filePath) throws Exception {
		String encoding = readFileAsStringAndGuessEncoding(filePath);
		return readFileAsStringCharFromPath(filePath, encoding);
	}

	// TODO update from TCM
	public static String readFileAsStringAndGuessEncoding(String filePath) throws Exception {
		String content_UTF_8 = readFileAsStringCharFromPath(filePath, EpeAppConstants.ENCODING_UTF_8);
		String content_ISO_8859_15 = readFileAsStringCharFromPath(filePath, EpeAppConstants.ENCODING_ISO_8859_15);

		if (content_ISO_8859_15.length() < content_UTF_8.length()) {
			return EpeAppConstants.ENCODING_ISO_8859_15;
		} else if (content_ISO_8859_15.length() > content_UTF_8.length()) {
			return EpeAppConstants.ENCODING_UTF_8;
		}

		char cZero = 216;
		char cSpecial1 = 138;
		char cSpecial2 = 65533;

		char[] specialChars = { '\u00C0', '\u00E0', '\u00C1', '\u00E1', // a
				'\u00C8', '\u00E8', '\u00C9', '\u00E9', // e
				'\u00CC', '\u00EC', '\u00CD', '\u00ED', // i
				'\u00D2', '\u00F2', '\u00D3', '\u00F3', // o
				'\u00D9', '\u00F9', '\u00DA', '\u00FA', // u
				cZero, // zero strano
				cSpecial1, cSpecial2, //
				'\u20AC' // euro, per ultimo, puo' creare ambiguita'
		};

		String commonCharsStr = "\n\t\r .,;:'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\\/|@#~!\"$%&()[]{}<>_-+/*=?¿^";
		String specialCharsStr = "\u00C0\u00E0\u00C1\u00E1\u00C8\u00E8\u00C9\u00E9\u00CC\u00EC\u00CD\u00ED"
				+ "\u00D2\u00F2\u00D3\u00F3\u00D9\u00F9\u00DA\u00FA\u20AC" + cZero + cSpecial1 + cSpecial2;

		for (char c : specialChars) {
			if (content_UTF_8.contains(c + "")) {
				validateChars(content_UTF_8, specialCharsStr, commonCharsStr);
				return EpeAppConstants.ENCODING_UTF_8;
			} else if (content_ISO_8859_15.contains(c + "")) {
				validateChars(content_ISO_8859_15, specialCharsStr, commonCharsStr);
				return EpeAppConstants.ENCODING_ISO_8859_15;
			}
		}

		validateChars(content_UTF_8, specialCharsStr, commonCharsStr);
		return EpeAppConstants.ENCODING_UTF_8;
	}

	public static void validateChars(String text, String specialCharsStr, String commonCharsStr)
			throws EpeAppException {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (commonCharsStr.contains(c + "")) {
				continue;
			} else if (specialCharsStr.contains(c + "")) {
				continue;
			}

			throw new EpeAppException("Unknown character \"" + c + "\" (int val " + ((int) c) + ")");
		}
	}

	/**
	 * From: http://snippets.dzone.com/posts/show/1335
	 *
	 * @param filePath
	 *            the name of the file to open. Not sure if it can accept URLs
	 *            or just filenames. Path handling could be better, and buffer
	 *            sizes are hardcoded
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public static String readFileAsStringCharFromPath(String filePath, String encoding) throws Exception {
		String ret;
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader;
		FileInputStream fis = null;
		encoding = encoding == null ? "UTF-8" : encoding;

		fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, encoding);
		reader = new BufferedReader(isr);

		char[] buf = new char[1024];
		int numRead;
		String readData;

		while ((numRead = reader.read(buf)) != -1) {
			readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		fis.close();
		reader.close();
		ret = fileData.toString();
		// ret = cleanCrlf(ret);

		return ret;
	}

	public static String getNotContainedString(String text, String open, String close) throws EpeAppException {
		EpeAppUtils.checkNull("text", text);
		EpeAppUtils.checkNull("open", open);
		EpeAppUtils.checkNull("close", close);
		int i = 0;

		while (text.contains(open + i + close)) {
			i++;
		}

		return open + i + close;
	}

	public static String getNotContainedString(String text) throws EpeAppException {
		return getNotContainedString(text, "{", "}");
	}

	public static String getNotContainedString(String notContained, int iter, String close) throws EpeAppException {
		EpeAppUtils.checkNull("notContained", notContained);
		notContained = notContained.substring(0, notContained.length() - 1) + "-" + iter + close;
		return notContained;
	}

	public static String getNotContainedString(String notContained, int iter) throws EpeAppException {
		return getNotContainedString(notContained, iter, "}");
	}

	public static String cleanFilename(String filename) {
		return filename.replace("\\", "/");
	}

	public static String[] retrieveLocationAndName(String fullPath) throws EpeAppException {
		EpeAppUtils.checkNull("fullPath", fullPath);

		if (!fullPath.contains("/")) {
			throw new EpeAppException("Full path " + fullPath + " does not have a /");
		}

		String[] ret = { null, null };
		String[] split = fullPath.split("/");
		ret[1] = split[split.length - 1];
		ret[0] = fullPath.substring(0, fullPath.length() - ret[1].length());

		return ret;
	}

}
