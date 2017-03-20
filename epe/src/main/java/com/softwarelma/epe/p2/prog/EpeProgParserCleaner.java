package com.softwarelma.epe.p2.prog;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeProgParserCleaner {

    private final EpeProgParserSearch parserSearch;

    protected EpeProgParserCleaner(EpeProgParserSearch parserSearch) {
        this.parserSearch = parserSearch;
    }

    protected String cleanProgramContent(String programContent, Map<String, String> mapNotContainedReplaced,
            String sDoubleBackSlash, String sBackSlashedQuote) throws EpeAppException {
        programContent = programContent.replace("\r\n", "\n");
        programContent = programContent.replace("\r", "");
        // programContent = cleanCommentOrString(programContent, true, mapNotContainedReplaced);
        programContent = programContent.replace("\\\\", sDoubleBackSlash);
        programContent = programContent.replace("\\\"", sBackSlashedQuote);
        programContent = cleanCommentOrString(programContent, mapNotContainedReplaced);
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

    private String cleanCommentOrString(String text, Map<String, String> mapNotContainedReplaced)
            throws EpeAppException {
        // System.out.println(text);
        // System.out.println("---------------------");
        String text2 = text;
        int iter = 0;
        String notContained = EpeAppUtils.getNotContainedString(text);

        do {
            text = text2;
            Boolean iscomment = this.isComment(text);

            if (iscomment == null) {
                return text;
            }

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

    private Boolean isComment(String text) {
        int[] posString = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_STR, "STR");
        int[] posLineComment = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_COMMENT_LINE, "COMMENT_LINE");
        int[] posBlockComment = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_COMMENT_BLOCK, "COMMENT_BLOCK");

        int posComment = this.retrieveValidMin(posLineComment[0], posBlockComment[0]);
        return this.isComment(posComment, posString[0]);
    }

    private Boolean isComment(int posComment, int posString) {
        if (posComment == -1 && posString == -1) {
            return null;
        } else if (posComment == -1) {
            return false;
        } else if (posString == -1) {
            return true;
        } else {
            return posComment < posString;
        }
    }

    private int retrieveValidMin(int pos0, int pos1) {
        if (pos0 == -1 && pos1 == -1) {
            return -1;
        } else if (pos0 == -1) {
            return pos1;
        } else if (pos1 == -1) {
            return pos0;
        } else {
            return Math.min(pos0, pos1);
        }
    }

    private String cleanStringOnce(String text, String notContained, int iter,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        // System.out.println(text);
        // System.out.println("---------------------");

        int[] posString = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_STR, "STR");
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

        int[] posLineComment = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_COMMENT_LINE, "COMMENT_LINE");
        posLineComment[1] = posLineComment[0] == -1 ? -1 : posLineComment[1] - 1;
        // System.out.println("posLineComment " + posLineComment[0] + ", " +
        // posLineComment[1]);
        int[] posBlockComment = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_COMMENT_BLOCK, "COMMENT_BLOCK");
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

}
