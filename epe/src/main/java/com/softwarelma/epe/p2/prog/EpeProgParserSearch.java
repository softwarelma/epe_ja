package com.softwarelma.epe.p2.prog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwarelma.epe.p1.app.EpeAppConstants;

public final class EpeProgParserSearch {

    protected int[] indexOf(String text, String patternStr) {
        boolean big = EpeAppConstants.REGEX_LIST_BIG.contains(patternStr);
        // patternStr.equals(EpeAppConstants.REGEX_STR_INTERNAL)
        // || patternStr.equals(EpeAppConstants.REGEX_LEFT_STR)
        // || patternStr.equals(EpeAppConstants.REGEX_STR)
        // || patternStr.equals(EpeAppConstants.REGEX_COMMENT_BLOCK);

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
