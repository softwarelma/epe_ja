package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalReplace extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "replace, expected the text (list or str) and {the list-list target-replacement and \n"
                + "optionally the maven-like bool (default false)} or {the target (list or str), the replacement \n"
                + "(list or str) and optionally the maven-like bool (default false)}.";

        if (isListListStringAt(listExecResult, 1, postMessage)) {
            EpeGenericFinalList_get_list_at list_get_list_at = new EpeGenericFinalList_get_list_at();
            List<EpeExecResult> listExecResult2 = new ArrayList<>();
            listExecResult2.add(listExecResult.get(1));

            listExecResult2.add(createResult("0"));
            EpeExecResult resultTarget = list_get_list_at.doFunc(execParams, listExecResult2);

            listExecResult2.set(1, createResult("1"));
            EpeExecResult resultReplacement = list_get_list_at.doFunc(execParams, listExecResult2);

            listExecResult.set(1, resultTarget);
            listExecResult.add(2, resultReplacement);

            return this.doFunc(execParams, listExecResult);
        }

        List<String> listText, listTarget, listReplacement, listStr;
        String text, target, replacement, str;
        String mavenLikeStr = getStringAt(listExecResult, 3, postMessage, "false");
        boolean mavenLike = EpeAppUtils.parseBoolean(mavenLikeStr);

        if (isStringAt(listExecResult, 0, postMessage)) {
            text = getStringAt(listExecResult, 0, postMessage);

            if (isStringAt(listExecResult, 1, postMessage)) {
                target = getStringAt(listExecResult, 1, postMessage);

                if (isStringAt(listExecResult, 2, postMessage)) {
                    replacement = getStringAt(listExecResult, 2, postMessage);
                    str = replace(mavenLike, text, target, replacement);
                } else if (isListStringAt(listExecResult, 2, postMessage)) {
                    listReplacement = getListStringAt(listExecResult, 2, postMessage);
                    str = replace(mavenLike, text, target, listReplacement);
                } else {
                    throw new EpeAppException("Replacement not valid. " + postMessage);
                }
            } else if (isListStringAt(listExecResult, 1, postMessage)) {
                listTarget = getListStringAt(listExecResult, 1, postMessage);

                if (isStringAt(listExecResult, 2, postMessage)) {
                    replacement = getStringAt(listExecResult, 2, postMessage);
                    str = replace(mavenLike, text, listTarget, replacement);
                } else if (isListStringAt(listExecResult, 2, postMessage)) {
                    listReplacement = getListStringAt(listExecResult, 2, postMessage);
                    str = replace(mavenLike, text, listTarget, listReplacement);
                } else {
                    throw new EpeAppException("Replacement not valid. " + postMessage);
                }
            } else {
                throw new EpeAppException("Target not valid. " + postMessage);
            }

            log(execParams, str);
            return createResult(str);
        } else if (isListStringAt(listExecResult, 0, postMessage)) {
            listText = getListStringAt(listExecResult, 0, postMessage);

            if (isStringAt(listExecResult, 1, postMessage)) {
                target = getStringAt(listExecResult, 1, postMessage);

                if (isStringAt(listExecResult, 2, postMessage)) {
                    replacement = getStringAt(listExecResult, 2, postMessage);
                    listStr = replace(mavenLike, listText, target, replacement);
                } else if (isListStringAt(listExecResult, 2, postMessage)) {
                    listReplacement = getListStringAt(listExecResult, 2, postMessage);
                    listStr = replace(mavenLike, listText, target, listReplacement);
                } else {
                    throw new EpeAppException("Replacement not valid. " + postMessage);
                }
            } else if (isListStringAt(listExecResult, 1, postMessage)) {
                listTarget = getListStringAt(listExecResult, 1, postMessage);

                if (isStringAt(listExecResult, 2, postMessage)) {
                    replacement = getStringAt(listExecResult, 2, postMessage);
                    listStr = replace(mavenLike, listText, listTarget, replacement);
                } else if (isListStringAt(listExecResult, 2, postMessage)) {
                    listReplacement = getListStringAt(listExecResult, 2, postMessage);
                    listStr = replace(mavenLike, listText, listTarget, listReplacement);
                } else {
                    throw new EpeAppException("Replacement not valid. " + postMessage);
                }
            } else {
                throw new EpeAppException("Target not valid. " + postMessage);
            }

            log(execParams, listStr);
            return createResult(listStr);
        }

        throw new EpeAppException("Text not valid. " + postMessage);
    }

    /**
     * MULTI TEXT
     * 
     * 1-1, multiple classic
     */
    public static List<String> replace(boolean mavenLike, List<String> listText, List<String> listTarget,
            List<String> listReplacement) {
        List<String> listTextNew = new ArrayList<>(listText.size());
        for (int i = 0; i < listText.size(); i++)
            listTextNew.add(replace(mavenLike, listText.get(i), listTarget, listReplacement));
        return listTextNew;
    }

    /**
     * MULTI TEXT
     * 
     * reducer
     */
    public static List<String> replace(boolean mavenLike, List<String> listText, List<String> listTarget,
            String replacement) {
        List<String> listTextNew = new ArrayList<>(listText.size());
        for (int i = 0; i < listText.size(); i++)
            listTextNew.add(replace(mavenLike, listText.get(i), listTarget, replacement));
        return listTextNew;
    }

    /**
     * MULTI TEXT
     * 
     * multiplier
     */
    public static List<String> replace(boolean mavenLike, List<String> listText, String target,
            List<String> listReplacement) {
        List<String> listTextNew = new ArrayList<>(listText.size());
        for (int i = 0; i < listText.size(); i++)
            listTextNew.add(replace(mavenLike, listText.get(i), target, listReplacement));
        return listTextNew;
    }

    /**
     * MULTI TEXT
     * 
     * classic
     */
    public static List<String> replace(boolean mavenLike, List<String> listText, String target, String replacement) {
        List<String> listTextNew = new ArrayList<>(listText.size());
        for (int i = 0; i < listText.size(); i++)
            listTextNew.add(replace(mavenLike, listText.get(i), target, replacement));
        return listTextNew;
    }

    /**
     * MONO TEXT
     * 
     * 1-1, multiple classic
     */
    public static String replace(boolean mavenLike, String text, List<String> listTarget,
            List<String> listReplacement) {
        for (int i = 0; i < listTarget.size(); i++)
            text = replace(mavenLike, text, listTarget.get(i), listReplacement.get(i));
        return text;
    }

    /**
     * MONO TEXT
     * 
     * reducer
     */
    public static String replace(boolean mavenLike, String text, List<String> listTarget, String replacement) {
        for (String target : listTarget)
            text = replace(mavenLike, text, target, replacement);
        return text;
    }

    /**
     * MONO TEXT
     * 
     * multiplier
     */
    public static String replace(boolean mavenLike, String text, String target, List<String> listReplacement) {
        for (String replacement : listReplacement)
            text = replace(mavenLike, text, target, replacement);
        return text;
    }

    /**
     * MONO TEXT
     * 
     * classic
     */
    public static String replace(boolean mavenLike, String text, String target, String replacement) {
        return mavenLike ? text.replace("${" + target + "}", replacement) : text.replace(target, replacement);
    }

}
