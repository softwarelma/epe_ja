package com.softwarelma.epe.p2.prog;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeProgParserSearch {

    private static final int gap = 20;
    private final Map<String, Pattern> mapPatternNameAndPattern = new HashMap<>();
    private static int count = 0;// FIXME remove

    protected int[] indexOf(String text, String patternStr, String patternName) throws EpeAppException {
        return indexOfWithIteration(text, patternStr, patternName);
    }

    protected int[] indexOfWithIteration(String text, String patternStr, String patternName) throws EpeAppException {
        boolean big = EpeAppConstants.REGEX_LIST_BIG.contains(patternStr);
        // patternStr.equals(EpeAppConstants.REGEX_STR_INTERNAL)
        // || patternStr.equals(EpeAppConstants.REGEX_LEFT_STR)
        // || patternStr.equals(EpeAppConstants.REGEX_STR)
        // || patternStr.equals(EpeAppConstants.REGEX_COMMENT_BLOCK);
        Pattern pattern = this.mapPatternNameAndPattern.get(patternName);

        if (pattern == null) {
            pattern = Pattern.compile(patternStr);
            this.mapPatternNameAndPattern.put(patternName, pattern);
            pattern = this.mapPatternNameAndPattern.get(patternName);
        }

        Matcher matcher = pattern.matcher(text);
        int[] ret = { -1, -1 };

        if (!big) {
            return matcher.find() ? new int[] { matcher.start(), matcher.end() } : ret;
        }

        // BIG

        if (!matcher.find()) {
            return ret;
        }

        int firstValidClosure = retrieveFirstValidClosure(text, patternName);
        // System.out.println("text=" + text);
        // System.out.println("firstValidClosure=" + firstValidClosure);

        for (int i = firstValidClosure; i < text.length(); i++) {
            count++;
            // System.out.println("i=" + i + ", patternName=" + patternName + ", patternStr=" + patternStr);
            String textPart = text.substring(0, i + 1);
            matcher = pattern.matcher(textPart);

            if (matcher.find()) {
                // System.out.println("textPart=" + textPart);
                System.out.println("ok count=" + count + ", patternName=" + patternName);
                return new int[] { matcher.start(), matcher.end() };
            }
        }

        System.out.println("KO count=" + count + ", patternName=" + patternName);
        return ret;
    }

    private int retrieveFirstValidClosure(String text, String patternName) throws EpeAppException {
        int firstValidClosure = -1;

        if ("STR".equals(patternName)) {
            int ind = text.indexOf("\"");

            if (ind != -1) {
                String textPart = text.substring(ind + 1);
                firstValidClosure = textPart.indexOf("\"");
                firstValidClosure = firstValidClosure == -1 ? -1 : firstValidClosure + ind + 1;
            }
        } else if ("COMMENT_BLOCK".equals(patternName)) {
            int ind = text.indexOf("*/");
            firstValidClosure = ind == -1 ? -1 : ind + 1;
        } else {
            throw new EpeAppException("Unknown patternName " + patternName);
        }

        EpeAppUtils.checkRange(firstValidClosure, 1, text.length(), false, true);
        return firstValidClosure;
    }

    protected int[] indexOfOld(String text, String patternStr, String patternName) {
        boolean big = EpeAppConstants.REGEX_LIST_BIG.contains(patternStr);
        // patternStr.equals(EpeAppConstants.REGEX_STR_INTERNAL)
        // || patternStr.equals(EpeAppConstants.REGEX_LEFT_STR)
        // || patternStr.equals(EpeAppConstants.REGEX_STR)
        // || patternStr.equals(EpeAppConstants.REGEX_COMMENT_BLOCK);
        Pattern pattern = this.mapPatternNameAndPattern.get(patternName);
        long ts0 = System.currentTimeMillis();

        if (pattern == null) {
            pattern = Pattern.compile(patternStr);
            this.mapPatternNameAndPattern.put(patternName, pattern);
            pattern = this.mapPatternNameAndPattern.get(patternName);
        }

        Matcher matcher = pattern.matcher(text);
        int[] ret = { -1, -1 };

        if (matcher.find()) {
            ret = new int[] { matcher.start(), matcher.end() };
        }

        if (big && ret[0] != -1) {
            String text2 = text.substring(0, ret[1] - 1);
            int[] ret2 = indexOfOld(text2, patternStr, patternName);
            ret = ret2[0] == -1 ? ret : ret2;
        }

        return ret;
    }

}
