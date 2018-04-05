package com.softwarelma.epe.p3.generic;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodingsResponse;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.disk.EpeDiskFinalFread_encoding;

public final class EpeGenericFinalReplace_encoding extends EpeGenericAbstract {

    public static final String PROP_PROP_FILE = "prop_file";

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "replace_encoding, expected the text (str), optionally the maven-like bool "
                + "(default false) and a prop (at any position) called "
                + EpeGenericFinalReplace_encoding.PROP_PROP_FILE;

        String propFile = retrievePropValueNotNull("replace_encoding", listExecResult,
                EpeGenericFinalReplace_encoding.PROP_PROP_FILE);
        listExecResult = retrieveNoProps(listExecResult, postMessage);

        String mavenLikeStr = getStringAt(listExecResult, 1, postMessage, "false");
        boolean mavenLike = EpeAppUtils.parseBoolean(mavenLikeStr);

        if (isStringAt(listExecResult, 0, postMessage)) {
            String text = getStringAt(listExecResult, 0, postMessage);
            return replaceEncoding(execParams, text, mavenLike, propFile);
        } else {
            throw new EpeAppException("Text not valid. " + postMessage);
        }
    }

    public static EpeExecResult replaceEncoding(EpeExecParams execParams, String text, boolean mavenLike,
            String propFile) throws EpeAppException {
        EpeEncodingsResponse response = EpeDiskFinalFread_encoding
                .fRead(execParams.getGlobalParams().isPrintToConsole(), propFile, null);
        List<List<String>> listListStr = EpeGenericFinalProp_text_to_list_list
                .propTextToListList(response.getFileContent());
        List<String> listTarget = listListStr.get(0);
        List<String> listReplacement = listListStr.get(1);
        List<String> listStr = new ArrayList<>();
        listStr.add(EpeGenericFinalReplace.replace(mavenLike, text, listTarget, listReplacement));
        listStr.add(response.getEncoding());
        log(execParams, listStr);
        return createResult(listStr);
    }

}
