package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalProp_text_to_list_list extends EpeGenericAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "prop_text_to_list_list, expected the text.";
        String text = getStringAt(listExecResult, 0, postMessage);
        List<List<String>> listListStr = propTextToListList(text);
        log(execParams, listListStr, "fake");
        return createResult(listListStr, "fake");
    }

    public static List<List<String>> propTextToListList(String text) throws EpeAppException {
        List<List<String>> listListStr = new ArrayList<>();
        List<String> listTarget = new ArrayList<>();
        List<String> listReplacement = new ArrayList<>();
        text = EpeAppUtils.retrieveVisualTrim(text);
        text = text.replace("\r\n", "\n");
        String[] arrayProp = text.split("\n");

        for (String prop : arrayProp) {
            prop = EpeAppUtils.retrieveVisualTrim(prop);
            if (prop.isEmpty() || prop.startsWith("#"))
                continue;
            Map.Entry<String, String> keyValueVisualTrim = EpeAppUtils.retrieveKeyAndValueVisualTrim(prop);
            listTarget.add(keyValueVisualTrim.getKey());
            listReplacement.add(keyValueVisualTrim.getValue());
        }

        listListStr.add(listTarget);
        listListStr.add(listReplacement);
        return listListStr;
    }

}
