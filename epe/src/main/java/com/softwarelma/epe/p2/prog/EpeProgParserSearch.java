package com.softwarelma.epe.p2.prog;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwarelma.epe.p1.app.EpeAppConstants;

public final class EpeProgParserSearch {

    private static final int gap = 20;
    private final Map<String, Pattern> mapPatternNameAndPattern = new HashMap<>();

    protected int[] indexOf(String text, String patternStr, String patternName) {
        return indexOf(text, patternStr, patternName, "");
    }

    protected int[] indexOf(String text, String patternStr, String patternName, String tab) {
        System.out.println(tab + "indexOf text=?" + ", patternName=" + patternName + ", patternStr=" + patternStr);

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
            if (matcher.find()) {
                ret = new int[] { matcher.start(), matcher.end() };
            }

            return ret;
        }

        // BIG

*        for (int i = text.length(); i > 0; i--) {
            System.out.println(tab + tab + "i=" + i + "indexOf text=?" + ", patternName=" + patternName
                    + ", patternStr=" + patternStr);
            String textPart = text.substring(0, i + 1);
            matcher = pattern.matcher(textPart);

            if (matcher.find()) {
                ret = new int[] { matcher.start(), matcher.end() };
                return ret;
            }
        }

        ret = new int[] { -1, -1 };
        return ret;
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

            long ts1 = System.currentTimeMillis();
            if (ts1 - ts0 > gap) {
                System.out.println("ts1: " + (ts1 - ts0) + ", " + patternName);
            }

            this.mapPatternNameAndPattern.put(patternName, pattern);
            pattern = this.mapPatternNameAndPattern.get(patternName);
        }

        Matcher matcher = pattern.matcher(text);

        long ts2 = System.currentTimeMillis();
        if (ts2 - ts0 > gap) {
            System.out.println("ts2: " + (ts2 - ts0) + ", " + patternName);
        }

        int[] ret = { -1, -1 };

        if (matcher.find()) {
            ret = new int[] { matcher.start(), matcher.end() };
        }

        long ts3 = System.currentTimeMillis();
        if (ts3 - ts2 > gap) {
            System.out.println("ts3: " + (ts3 - ts2) + ", " + patternName);
        }

        if (big && ret[0] != -1) {
            String text2 = text.substring(0, ret[1] - 1);
            int[] ret2 = indexOfOld(text2, patternStr, patternName);
            ret = ret2[0] == -1 ? ret : ret2;
        }

        return ret;
    }

}
