package com.softwarelma.epe.p2.prog;

import java.util.HashMap;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeProgParserCleaner {

    private EpeProgParserSearch parserSearch;

    protected EpeProgParserCleaner(EpeProgParserSearch parserSearch) {
        this.parserSearch = parserSearch;
    }

    protected String cleanProgramContent(String programContent, Map<String, String> mapNotContainedReplaced,
            String sDoubleBackSlash, String sBackSlashedQuote, Map<String, String> mapComments) throws EpeAppException {
        programContent = programContent.replace("\r\n", "\n");
        programContent = programContent.replace("\r", "");
        // programContent = cleanCommentOrString(programContent, true, mapNotContainedReplaced);
        programContent = programContent.replace("\\\\", sDoubleBackSlash);
        programContent = programContent.replace("\\\"", sBackSlashedQuote);
        programContent = cleanCommentOrString(programContent, mapNotContainedReplaced, mapComments);
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

    public String cleanComment(String text) throws EpeAppException {
        Map<String, String> mapComments = new HashMap<>();
        return this.cleanComment(text, mapComments);
    }

    public String cleanComment(String text, Map<String, String> mapComments) throws EpeAppException {
        String text2 = text;

        do {
            text = text2;
            text2 = cleanCommentOnce(text, mapComments, false);
        } while (text2 != null);

        return text;
    }

    private String cleanCommentOrString(String text, Map<String, String> mapNotContainedReplaced,
            Map<String, String> mapComments) throws EpeAppException {
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
                text2 = cleanCommentOnce(text, mapComments, true);
            } else {
                text2 = cleanStringOnce(text, notContained, iter++, mapNotContainedReplaced);
            }

        } while (text2 != null);

        return text;
    }

    private Boolean isComment(String text) throws EpeAppException {
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

        int[] posString = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_STR, "STR");
        // int[] posString = indexOf(text, "\"(((?!\").)*\\n*)*\"", 0, 3);

        // println("posBlockComment " + posBlockComment[0] + ", " +
        // posBlockComment[1]);

        if (posString[0] == -1) {
            return null;
        }

        notContained = EpeAppUtils.getNotContainedString(notContained, iter);
        String replaced = text.substring(posString[0], posString[1]);
        mapNotContainedReplaced.put(notContained, replaced);
        text = text.replace(replaced, notContained);
        // text = text.substring(0, posString[0]) + notContained +
        // text.substring(posString[1], text.length());
        return text;
    }

    private String cleanCommentOnce(String text, Map<String, String> mapComments, boolean doLoad)
            throws EpeAppException {
        int[] posLineComment = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_COMMENT_LINE, "COMMENT_LINE");
        posLineComment[1] = posLineComment[0] == -1 ? -1 : posLineComment[1] - 1;
        int[] posBlockComment = this.parserSearch.indexOf(text, EpeAppConstants.REGEX_COMMENT_BLOCK, "COMMENT_BLOCK");
        int[] posComment;

        if (posLineComment[0] == -1 && posBlockComment[0] == -1) {
            posComment = posLineComment;
        } else if (posLineComment[0] != -1 && posBlockComment[0] != -1) {
            posComment = posLineComment[0] < posBlockComment[0] ? posLineComment : posBlockComment;
        } else {
            posComment = posLineComment[0] > posBlockComment[0] ? posLineComment : posBlockComment;
        }

        if (posComment[0] == -1)
            return null;
        if (doLoad)
            this.loadComment(text, posComment, mapComments);
        text = text.substring(0, posComment[0]) + text.substring(posComment[1], text.length());
        return text;
    }

    private void loadComment(String text, int[] posComment, Map<String, String> mapComments) throws EpeAppException {
        EpeAppUtils.checkNull("mapComments", mapComments);
        String comment = text.substring(posComment[0], posComment[1]);
        comment = comment.startsWith("//") ? comment.substring(2) : comment.substring(2, comment.length() - 2);
        comment = EpeAppUtils.retrieveVisualTrim(comment);
        if (!comment.contains("="))
            return;
        Map.Entry<String, String> keyValueVisualTrim;

        boolean showExceptions = EpeAppConstants.SHOW_EXCEPTIONS;
        EpeAppConstants.SHOW_EXCEPTIONS = false;
        try {
            keyValueVisualTrim = EpeAppUtils.retrieveKeyAndValueVisualTrim(comment);
        } catch (EpeAppException e) {
            EpeAppConstants.SHOW_EXCEPTIONS = showExceptions;
            return;
        }
        EpeAppConstants.SHOW_EXCEPTIONS = showExceptions;

        // if the key is a valid id
        String key = keyValueVisualTrim.getKey();
        int[] posString = this.parserSearch.indexOf(key, EpeAppConstants.REGEX_ID, "ID");
        if (posString[0] == 0 && posString[1] == key.length())
            mapComments.put(key, keyValueVisualTrim.getValue());
    }

}
