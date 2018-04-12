package com.softwarelma.epe.p2.prog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppConstants.SENT_TYPE;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeProgParserSent {

    private final EpeProgParserSearch parserSearch;

    protected EpeProgParserSent(EpeProgParserSearch parserSearch) {
        this.parserSearch = parserSearch;
    }

    protected EpeProgSentInterface getProgSent(String sentStr, Map<String, String> mapNotContainedReplaced,
            String sDoubleBackSlash, String sBackSlashedQuote) throws EpeAppException {
        String originalSentStr = sentStr;
        sentStr = sentStr.replace(" ", "");
        sentStr = sentStr.replace("\n", "");
        sentStr = sentStr.replace("\r", "");
        sentStr = sentStr.replace("\t", "");

        // String reRight = "(" + "(" + reFunc + ")|(" + reNull + ")|(" + reStr
        // + ")|(" + reId + ")" + ")";
        int[] posString;
        SENT_TYPE sentType;
        EpeProgSentInterface progSent;
        String[] textSplit;

        // posString = this.parserSearch.indexOf(sentStr, EpeAppConstants.REGEX_NULL);
        // sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "null_type" : null;
        //
        // if (sentType != null) {
        // progSent = new EpeProgDefaultSent(sentType, null, sentStr, null, null);
        // // println(" " + progSent);
        // return progSent;
        // }

        posString = this.parserSearch.indexOf(sentStr, EpeAppConstants.REGEX_STR_INTERNAL, "STR_INTERNAL");
        sentType = posString[0] == 0 && posString[1] == sentStr.length() ? SENT_TYPE.str : null;

        if (sentType != null) {
            String literal = mapNotContainedReplaced.get(sentStr);
            // String literal = mapNotContainedReplaced.get(mapNotContainedReplaced.get(sentStr));
            literal = literal.replace(sBackSlashedQuote, "\\\"");
            literal = literal.replace(sDoubleBackSlash, "\\\\");
            progSent = new EpeProgDefaultSent(originalSentStr, sentType, null, literal, null);
            // println(" " + progSent);
            return progSent;
        }

        posString = this.parserSearch.indexOf(sentStr, EpeAppConstants.REGEX_FUNC, "FUNC");
        sentType = posString[0] == 0 && posString[1] == sentStr.length() ? SENT_TYPE.func : null;

        if (sentType != null) {
            progSent = getProgSentFromFuncSent(originalSentStr, sentStr, sentType, mapNotContainedReplaced,
                    sDoubleBackSlash, sBackSlashedQuote);
            // println(" " + progSent);
            return progSent;
        }

        posString = this.parserSearch.indexOf(sentStr, EpeAppConstants.REGEX_ID, "ID");
        sentType = posString[0] == 0 && posString[1] == sentStr.length() ? SENT_TYPE.id : null;

        if (sentType != null) {
            progSent = new EpeProgDefaultSent(originalSentStr, sentType, null, sentStr, null);
            // println(" " + progSent);
            return progSent;
        }

        // posString = this.parserSearch.indexOf(sentStr, EpeAppConstants.REGEX_LEFT_NULL);
        // sentType = posString[0] == 0 && posString[1] == sentStr.length() ? "left_null_type" : null;
        //
        // if (sentType != null) {
        // textSplit = sentStr.split("=");
        // progSent = new EpeProgDefaultSent(sentType, textSplit[0], textSplit[1], null, null);
        // // println(" " + progSent);
        // return progSent;
        // }

        posString = this.parserSearch.indexOf(sentStr, EpeAppConstants.REGEX_LEFT_STR, "LEFT_STR");
        sentType = posString[0] == 0 && posString[1] == sentStr.length() ? SENT_TYPE.left_str : null;

        if (sentType != null) {
            textSplit = sentStr.split("=");
            String literal = mapNotContainedReplaced.get(textSplit[1]);
            literal = literal.replace(sBackSlashedQuote, "\\\"");
            literal = literal.replace(sDoubleBackSlash, "\\\\");
            progSent = new EpeProgDefaultSent(originalSentStr, sentType, textSplit[0], literal, null);
            // println(" " + progSent);
            return progSent;
        }

        posString = this.parserSearch.indexOf(sentStr, EpeAppConstants.REGEX_LEFT_FUNC, "LEFT_FUNC");
        sentType = posString[0] == 0 && posString[1] == sentStr.length() ? SENT_TYPE.left_func : null;

        if (sentType != null) {
            progSent = getProgSentFromFuncSent(originalSentStr, sentStr, sentType, mapNotContainedReplaced,
                    sDoubleBackSlash, sBackSlashedQuote);
            // println(" " + progSent);
            return progSent;
        }

        posString = this.parserSearch.indexOf(sentStr, EpeAppConstants.REGEX_LEFT_ID, "LEFT_ID");
        sentType = posString[0] == 0 && posString[1] == sentStr.length() ? SENT_TYPE.left_id : null;

        if (sentType != null) {
            textSplit = sentStr.split("=");
            progSent = new EpeProgDefaultSent(originalSentStr, sentType, textSplit[0], textSplit[1], null);
            // println(" " + progSent);
            return progSent;
        }

        sentStr = EpeAppUtils.replaceNotContainedWithReplaced(sentStr, mapNotContainedReplaced);
        sentStr = sentStr.replace(sBackSlashedQuote, "\\\"");
        sentStr = sentStr.replace(sDoubleBackSlash, "\\\\");
        throw new EpeAppException("Unrecognized sentence: " + sentStr);
    }

    private EpeProgSentInterface getProgSentFromFuncSent(String originalSentStr, String funcSent, SENT_TYPE sentType,
            Map<String, String> mapNotContainedReplaced, String sDoubleBackSlash, String sBackSlashedQuote)
            throws EpeAppException {
        String leftSideVarName = null;
        String[] textSplit;

        if (funcSent.contains("=")) {
            textSplit = funcSent.split("=");
            leftSideVarName = textSplit[0];
            funcSent = textSplit[1];
        }

        textSplit = funcSent.split("\\(");
        String funcName = textSplit[0];
        String params = funcSent.substring(funcName.length() + 1, funcSent.length() - 1);
        List<EpeProgSentInterface> listParam = new ArrayList<>();
        String currentParam;

        while (!(currentParam = this.getNextParam(params)).isEmpty()) {
            params = params.substring(currentParam.length());
            params = params.startsWith(",") ? params.substring(1) : params;
            EpeProgSentInterface currentProgSent = this.getProgSent(currentParam, mapNotContainedReplaced,
                    sDoubleBackSlash, sBackSlashedQuote);
            listParam.add(currentProgSent);
        }

        EpeProgSentInterface progSent = new EpeProgDefaultSent(originalSentStr, sentType, leftSideVarName, funcName,
                listParam);
        return progSent;
    }

    private String getNextParam(String params) {
        String nextParam = "";
        int bracketPair = 0;

        for (int i = 0; i < params.length(); i++) {
            char c = params.charAt(i);

            if (c == '(') {
                bracketPair++;
            } else if (c == ')') {
                bracketPair--;
            } else if (c == ',') {
                if (bracketPair == 0) {
                    return nextParam;
                }
            }

            nextParam += c;
        }

        return nextParam;
    }

}
