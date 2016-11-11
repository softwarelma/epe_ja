package com.softwarelma.epe.p1.app;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public static void checkEquals(String name1, String name2, Object value1, Object value2) throws EpeAppException {
        checkNull("name1", name1);
        checkNull("name2", name2);

        if (value1 == null && value2 == null) {
            return;
        }

        if (value1 == null || value2 == null || !value1.equals(value2)) {
            throw new EpeAppException("Vars " + name1 + " and " + name2 + " are not equals");
        }
    }

    public static void checkContains(List<String> list, String name, String value) throws EpeAppException {
        checkNull("name", name);
        checkNull("list", list);

        if (!list.contains(value)) {
            throw new EpeAppException(name + " \"" + value + "\" is not contained in " + list);
        }
    }

    public static void checkContains(String[] array, String name, String value) throws EpeAppException {
        checkNull("name", name);
        checkNull("array", array);
        List<String> list = Arrays.asList(array);
        checkContains(list, name, value);
    }

    public static void checkRange(int i, int i0, int i1, boolean open0, boolean open1) throws EpeAppException {
        checkRange(i, i0, i1, open0, open1, "");
    }

    public static void checkRange(int i, int i0, int i1, boolean open0, boolean open1, String postMessage)
            throws EpeAppException {
        if (open0) {
            if (i < i0) {
                throw new EpeAppException("Out of range, value " + i + " < " + i0 + ". " + postMessage);
            } else if (i == i0) {
                throw new EpeAppException("Out of range, value " + i + " == " + i0 + ". " + postMessage);
            }
        } else {
            if (i < i0) {
                throw new EpeAppException("Out of range, value " + i + " < " + i0 + ". " + postMessage);
            }
        }

        if (open1) {
            if (i > i1) {
                throw new EpeAppException("Out of range, value " + i + " > " + i1 + ". " + postMessage);
            } else if (i == i1) {
                throw new EpeAppException("Out of range, value " + i + " == " + i1 + ". " + postMessage);
            }
        } else {
            if (i > i1) {
                throw new EpeAppException("Out of range, value " + i + " > " + i1 + ". " + postMessage);
            }
        }
    }

    public static <T> void checkSize(String name, List<T> list, int size) throws EpeAppException {
        checkNull(name, list);

        if (list.size() != size) {
            throw new EpeAppException("Wrong list size: " + list.size() + " != " + size);
        }
    }

    public static void checkEmpty(String paramName, Object paramValue) throws EpeAppException {
        checkNull("paramName", paramName);

        if (paramValue == null || paramValue.toString().trim().isEmpty()) {
            throw new EpeAppException("Il parametro " + paramName + " non puo' essere vuoto");
        }
    }

    public static void checkEmptyForceEmpty(String paramName, Object paramValue, boolean doLog) throws EpeAppException {
        checkNull("paramName", paramName);

        if (paramValue != null && !paramValue.toString().trim().isEmpty()) {
            throw new EpeAppException("Il parametro " + paramName + " dev'essere vuoto, trovato: " + paramValue);
        }
    }

    public static void checkEmptyNoTrim(String paramName, Object paramValue) throws EpeAppException {
        checkNull("paramName", paramName);

        if (paramValue == null || paramValue.toString().isEmpty()) {
            throw new EpeAppException("Il parametro " + paramName + " non puo' essere vuoto");
        }
    }

    public static void checkEmptyForceEmpty(String paramName, Object paramValue) throws EpeAppException {
        checkEmptyForceEmpty(paramName, paramValue, true);
    }

    public static <T> void checkEmptyArray(String paramName, T[] paramValue) throws EpeAppException {
        checkEmpty("paramName", paramName);

        if (paramValue == null || paramValue.length == 0) {
            throw new EpeAppException("Il parametro " + paramName + " non puo' essere vuoto");
        }
    }

    public static <T> void checkEmptyList(String paramName, List<T> paramValue) throws EpeAppException {
        checkNull("paramName", paramName);

        if (paramValue == null || paramValue.isEmpty()) {
            throw new EpeAppException("Il parametro " + paramName + " non puo' essere vuoto");
        }
    }

    public static void checkDir(File dir) throws EpeAppException {
        checkNull("dir", dir);

        if (!dir.isDirectory()) {
            throw new EpeAppException("The file \"" + dir.getName() + "\" is not a dir");
        }
    }

    public static <T> boolean isEmptyArray(T[] param) throws EpeAppException {
        return param == null || param.length == 0;
    }

    public static boolean isEmpty(String param) {
        return param == null || param.isEmpty();
    }

    public static boolean isEmptyTrimming(String param) {
        return param == null || param.isEmpty() || param.trim().isEmpty()
                || param.trim().replace("\t", "").replace("\r\n", "").replace("\n", "").isEmpty();
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
        return getNotContainedString(text, EpeAppConstants.CONTAINED_STRING_OPEN,
                EpeAppConstants.CONTAINED_STRING_CLOSE);
    }

    public static String getNotContainedString(String notContained, int iter, String close) throws EpeAppException {
        EpeAppUtils.checkNull("notContained", notContained);
        notContained = notContained.substring(0, notContained.length() - 1) + "-" + iter + close;
        return notContained;
    }

    public static String getNotContainedString(String notContained, int iter) throws EpeAppException {
        return getNotContainedString(notContained, iter, EpeAppConstants.CONTAINED_STRING_CLOSE);
    }

    public static String cleanFilename(String filename) throws EpeAppException {
        checkNull("filename", filename);
        return filename.replace("\\", "/");
    }

    public static String cleanDirName(String dirName) throws EpeAppException {
        checkNull("dirName", dirName);
        dirName = cleanFilename(dirName);
        dirName = dirName.endsWith("/") ? dirName : dirName + "/";
        return dirName;
    }

    public static Map.Entry<String, String> retrieveFilePathAndName(String fullFileName) throws EpeAppException {
        checkEmpty("fullFileName", fullFileName);
        fullFileName = cleanFilename(fullFileName);
        fullFileName = fullFileName.endsWith("/") ? fullFileName.substring(0, fullFileName.length() - 1) : fullFileName;
        String[] array = fullFileName.split("/");

        String name = array[array.length - 1];
        String path = fullFileName.substring(0, fullFileName.length() - name.length());

        Map.Entry<String, String> filePathAndName = new AbstractMap.SimpleEntry<>(path, name);
        return filePathAndName;
    }

    public static String replaceNotContainedWithReplaced(String sentStr, Map<String, String> mapNotContainedReplaced) {
        for (String notContained : mapNotContainedReplaced.keySet()) {
            sentStr = sentStr.replace(notContained, mapNotContainedReplaced.get(notContained));
        }

        return sentStr;
    }

    public static int parseInt(String str) throws EpeAppException {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new EpeAppException("parseInt for str " + str, e);
        }
    }

    public static boolean parseBoolean(String str) throws EpeAppException {
        try {
            return Boolean.parseBoolean(str);
        } catch (NumberFormatException e) {
            throw new EpeAppException("parseBoolean for str " + str, e);
        }
    }

    public static <T> List<T> asList(T[] array) {
        List<T> list = new ArrayList<T>();

        for (T t : array) {
            list.add(t);
        }

        return list;
    }

}
